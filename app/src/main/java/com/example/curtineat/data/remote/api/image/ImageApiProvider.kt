package com.example.curtineat.data.remote.api.image

import retrofit2.Retrofit

object ImageApiProvider {

	private const val BASE_URL =
		"https://curtineat-image-api.work-gordonyewyangliew.workers.dev/"

	private val retrofit: Retrofit = Retrofit.Builder()
		.baseUrl(BASE_URL)
		.build()

	val service: ImageApiService =
		retrofit.create(ImageApiService::class.java)
}