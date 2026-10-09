
package com.example.curtineat.data.repository.api

import android.content.Context
import android.net.Uri
import com.example.curtineat.data.repository.image.ImageCacheRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ImageUploadRepository(
	private val context: Context,
	private val imageRepository: ImageRepository,
	private val imageCache: ImageCacheRepository
) {
	suspend fun uploadImage(uri: Uri): String =
		withContext(Dispatchers.IO) {
			val file = File.createTempFile(
				"profile_image",
				".jpg",
				context.cacheDir
			)

			try {
				val inputStream = context.contentResolver
					.openInputStream(uri)
					?: throw IllegalArgumentException(
						"Unable to open selected image"
					)

				inputStream.use { input ->
					file.outputStream().use { output ->
						input.copyTo(output)
					}
				}

				val imageBytes = file.readBytes()

				require(imageBytes.isNotEmpty()) {
					"Selected image is empty"
				}

				val requestBody = file.asRequestBody(
					"image/*".toMediaType()
				)

				val imagePart = MultipartBody.Part.createFormData(
					"image",
					file.name,
					requestBody
				)

				val imageId = imageRepository.uploadImage(imagePart)

				imageCache.cacheImage(
					imageId = imageId,
					imageBytes = imageBytes
				)

				imageId
			} catch (exception: CancellationException) {
				throw exception
			} finally {
				file.delete()
			}
		}
}