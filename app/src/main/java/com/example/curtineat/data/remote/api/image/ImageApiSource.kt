package com.example.curtineat.data.remote.api.image

import okhttp3.MultipartBody
import okhttp3.ResponseBody

class ImageApiSource(
	private val service: ImageApiService
) {

	suspend fun uploadImage(
		image: MultipartBody.Part
	): String {
		return service.uploadImage(image).imageId
	}

	suspend fun getImage(imageId: String): ResponseBody {
		val response = service.getImage(imageId)
		if (!response.isSuccessful) {
			throw java.io.IOException("GET images/$imageId -> HTTP ${response.code()}")
		}
		return response.body()
			?: throw java.io.IOException("GET images/$imageId -> empty body")
	}
}