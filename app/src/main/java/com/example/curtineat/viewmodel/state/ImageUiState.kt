
package com.example.curtineat.viewmodel.state

sealed interface ImageUiState {
	data object Loading : ImageUiState

	data class Loaded(
		val bytes: ByteArray
	) : ImageUiState

	data object Unavailable : ImageUiState
}