package com.example.curtineat.viewmodel.state

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ImageUiState {
	data object Loading : ImageUiState

	@Immutable
	class Loaded(
		val bytes: ByteArray
	) : ImageUiState

	data object Unavailable : ImageUiState
}