package com.example.curtineat.data.remote.api.image

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ImageApiService {

	@Multipart
	@POST("images")
	suspend fun uploadImage(
		@Part image: MultipartBody.Part
	): ImageResponse

	@GET("images/{imageId}")
	suspend fun getImage(
		@Path("imageId") imageId: String
	): Response<ResponseBody>
}