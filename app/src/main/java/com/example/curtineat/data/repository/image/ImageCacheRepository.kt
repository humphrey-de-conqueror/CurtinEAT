
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
	private val remote: ImageRepository,
	private val maxCacheBytes: Long = DEFAULT_MAX_CACHE_BYTES
) {
	private val imageLocks = ConcurrentHashMap<String, Mutex>()

	// Protect cache writes and eviction across different image requests.
	private val cacheMutationLock = Mutex()

	init {
		require(maxCacheBytes > 0) {
			"Maximum image cache size must be greater than zero"
		}
	}

	suspend fun getImageBytes(imageId: String): ByteArray? {
		if (imageId.isBlank()) {
			return null
		}

		imageDao.getById(imageId)?.let { cached ->
			return cached.imageBytes
		}

		val imageLock = imageLocks.getOrPut(imageId) { Mutex() }

		return imageLock.withLock {
			// Another request may have populated the cache while we waited.
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

				cacheMutationLock.withLock {
					imageDao.upsert(
						CachedImageEntity(
							imageId = imageId,
							imageBytes = imageBytes,
							cachedAt = System.currentTimeMillis()
						)
					)

					evictUntilWithinLimit()
				}

				imageBytes
			} catch (exception: CancellationException) {
				throw exception
			} catch (exception: Exception) {
				null
			}
		}
	}

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

		cacheMutationLock.withLock {
			imageDao.upsert(
				CachedImageEntity(
					imageId = imageId,
					imageBytes = imageBytes,
					cachedAt = System.currentTimeMillis()
				)
			)

			evictUntilWithinLimit()
		}
	}

	suspend fun removeCachedImage(imageId: String) {
		imageDao.deleteById(imageId)
	}

	suspend fun getCacheSizeBytes(): Long =
		imageDao.getTotalBytes()

	suspend fun clearCache() {
		imageDao.deleteAll()
	}

	private suspend fun evictUntilWithinLimit() {
		while (imageDao.getTotalBytes() > maxCacheBytes) {
			imageDao.deleteOldest(1)
		}
	}

	companion object {
		const val DEFAULT_MAX_CACHE_BYTES: Long =
			100L * 1024L * 1024L
	}
}