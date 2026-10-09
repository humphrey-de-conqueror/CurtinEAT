package com.example.curtineat.data.repository.auth

import com.example.curtineat.viewmodel.UserRole

sealed interface LoginResult {

	data class Success(
		val userId: String,
		val role: UserRole
	) : LoginResult

	data object InvalidCredentials : LoginResult

	data object WrongRole : LoginResult

	data object Error : LoginResult
}