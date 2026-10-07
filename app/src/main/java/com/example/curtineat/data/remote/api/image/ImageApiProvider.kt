package com.example.curtineat.data.remote.api.image

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ImageApiProvider {

	private const val BASE_URL =
		"https://curtineat-image-api.work-gordonyewyangliew.workers.dev/"

	private val json = Json {
		ignoreUnknownKeys = true
	}

	private val retrofit: Retrofit = Retrofit.Builder()
		.baseUrl(BASE_URL)
		.addConverterFactory(
			json.asConverterFactory(
				"application/json".toMediaType()
			)
		)
		.build()

	val service: ImageApiService =
		retrofit.create(ImageApiService::class.java)
}