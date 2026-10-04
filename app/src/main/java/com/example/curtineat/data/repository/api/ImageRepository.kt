package com.example.curtineat.data.repository.api

import com.example.curtineat.data.remote.api.image.ImageApiSource
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class ImageRepository(
	private val source: ImageApiSource
) {

	suspend fun uploadImage(
		image: MultipartBody.Part
	): String {
		return source.uploadImage(image)
	}

	suspend fun getImage(
		imageId: String
	): ResponseBody {
		return source.getImage(imageId)
	}
}