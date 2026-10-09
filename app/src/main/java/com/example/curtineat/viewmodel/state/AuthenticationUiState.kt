package com.example.curtineat.viewmodel.state

import com.example.curtineat.viewmodel.UserRole

data class AuthenticationUiState(
	val userId: String? = null,
	val role: UserRole = UserRole.NONE,
	val isLoading: Boolean = false,
	val errorMessage: String? = null
)