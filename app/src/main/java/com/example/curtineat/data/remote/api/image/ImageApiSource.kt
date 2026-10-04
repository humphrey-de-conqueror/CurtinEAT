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

	suspend fun getImage(
		imageId: String
	): ResponseBody {
		return service.getImage(imageId).body()
			?: throw Exception("Image response is empty")
	}
}