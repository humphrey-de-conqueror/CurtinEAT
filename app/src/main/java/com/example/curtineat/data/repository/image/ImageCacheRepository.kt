
package com.example.curtineat.data.repository.image

import android.graphics.BitmapFactory
import android.util.Log
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
		if (imageId.isBlank()) return null

		readCache(imageId)?.let { return it }

		val imageLock = imageLocks.getOrPut(imageId) { Mutex() }

		return imageLock.withLock {
			readCache(imageId)?.let { return@withLock it }

			try {
				val imageBytes = withContext(Dispatchers.IO) {
					remote.getImage(imageId).use { it.bytes() }
				}

				if (!isDecodableImage(imageBytes)) {
					Log.w(TAG, "Not an image: $imageId (${imageBytes.size} bytes)")
					return@withLock null
				}

				if (imageBytes.size <= MAX_ROW_BYTES) {
					cacheMutationLock.withLock {
						imageDao.upsert(
							CachedImageEntity(imageId, imageBytes, System.currentTimeMillis())
						)
						evictUntilWithinLimit()
					}
				}
				imageBytes
			} catch (e: CancellationException) {
				throw e
			} catch (e: Exception) {
				Log.w(TAG, "Download failed for $imageId", e)
				null
			}
		}
	}

	// Never throws; removes bad entries so they can't poison the cache.
	private suspend fun readCache(imageId: String): ByteArray? = try {
		val cached = imageDao.getById(imageId)
		when {
			cached == null -> null
			isDecodableImage(cached.imageBytes) -> cached.imageBytes
			else -> {
				Log.w(TAG, "Dropping corrupt cache entry: $imageId")
				imageDao.deleteById(imageId)
				null
			}
		}
	} catch (e: CancellationException) {
		throw e
	} catch (e: Exception) {
		Log.w(TAG, "Cache read failed for $imageId", e)   // e.g. row too big
		runCatching { imageDao.deleteById(imageId) }
		null
	}

	private fun isDecodableImage(bytes: ByteArray): Boolean {
		if (bytes.isEmpty()) return false
		val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
		BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opts)
		return opts.outWidth > 0 && opts.outHeight > 0
	}

	companion object {
		private const val TAG = "ImageCache"
		private const val MAX_ROW_BYTES = 1_500_000
		const val DEFAULT_MAX_CACHE_BYTES: Long = 100L * 1024L * 1024L
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

		// Rows over ~2 MB cannot be read back from Room (CursorWindow limit).
		// Skip caching; the image is downloaded on demand instead.
		if (imageBytes.size > MAX_ROW_BYTES) {
			Log.w(TAG, "Not caching $imageId: ${imageBytes.size} bytes is too big")
			return
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
}