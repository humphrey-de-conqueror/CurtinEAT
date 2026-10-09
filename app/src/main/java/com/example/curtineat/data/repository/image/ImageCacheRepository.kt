package com.example.curtineat.data.repository.image

import com.example.curtineat.data.local.room.dao.CachedImageDao
import com.example.curtineat.data.local.room.entity.CachedImageEntity
import com.example.curtineat.data.repository.api.ImageRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

class ImageCacheRepository(
	private val imageDao: CachedImageDao,
	private val remote: ImageRepository
) {
	private val imageLocks = ConcurrentHashMap<String, Mutex>()

	/**
	 * Returns cached image bytes when available.
	 * Downloads and caches the image on a cache miss.
	 *
	 * Returns null when the image cannot be retrieved.
	 */
	suspend fun getImageBytes(imageId: String): ByteArray? {
		if (imageId.isBlank()) {
			return null
		}

		// Fast path: return the existing cache entry.
		imageDao.getById(imageId)?.let { cached ->
			return cached.imageBytes
		}

		// Prevent simultaneous requests for the same image.
		val lock = imageLocks.getOrPut(imageId) { Mutex() }

		return lock.withLock {
			// Another coroutine may have populated the cache while we waited.
			imageDao.getById(imageId)?.let { cached ->
				return@withLock cached.imageBytes
			}

			try {
				val imageBytes = withContext(Dispatchers.IO) {
					remote.getImage(imageId).use { responseBody ->
						responseBody.bytes()
					}
				}

				if (imageBytes.isEmpty()) {
					return@withLock null
				}

				imageDao.upsert(
					CachedImageEntity(
						imageId = imageId,
						imageBytes = imageBytes,
						cachedAt = System.currentTimeMillis()
					)
				)

				imageBytes
			} catch (exception: CancellationException) {
				throw exception
			} catch (exception: Exception) {
				// Preserve the app's ability to show a placeholder.
				// The next request can retry the network fetch.
				null
			}
		}
	}

	/**
	 * Stores image bytes already available locally,
	 * for example after an image has been selected or processed.
	 */
	suspend fun cacheImage(
		imageId: String,
		imageBytes: ByteArray
	) {
		require(imageId.isNotBlank()) {
			"Image ID must not be blank"
		}

		require(imageBytes.isNotEmpty()) {
			"Image bytes must not be empty"
		}

		imageDao.upsert(
			CachedImageEntity(
				imageId = imageId,
				imageBytes = imageBytes,
				cachedAt = System.currentTimeMillis()
			)
		)
	}

	suspend fun removeCachedImage(imageId: String) {
		imageDao.deleteById(imageId)
	}

	suspend fun getCacheSizeBytes(): Long {
		return imageDao.getTotalBytes()
	}

	suspend fun clearCache() {
		imageDao.deleteAll()
	}
}