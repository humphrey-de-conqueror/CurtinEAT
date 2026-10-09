
package com.example.curtineat.viewmodel.state

data class DataUiState<T>(
	val data: T,
	val isInitialLoading: Boolean = false,
	val isRefreshing: Boolean = false,
	val hasLoaded: Boolean = false,
	val errorMessage: String? = null
)