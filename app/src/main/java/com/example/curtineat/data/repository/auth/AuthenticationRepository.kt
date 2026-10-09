package com.example.curtineat.data.repository.auth

import com.example.curtineat.data.remote.firebase.model.FirebaseCustomerData
import com.example.curtineat.data.remote.firebase.model.FirebaseVendorData
import com.example.curtineat.data.remote.firebase.source.FirebaseAuthSource
import com.example.curtineat.data.repository.firebase.FirebaseCustomerRepository
import com.example.curtineat.data.repository.firebase.FirebaseVendorRepository
import com.example.curtineat.viewmodel.RegisterResult
import com.example.curtineat.viewmodel.UserRole
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class AuthenticationRepository(
	private val authSource: FirebaseAuthSource,
	private val customerRemote: FirebaseCustomerRepository,
	private val vendorRemote: FirebaseVendorRepository
) {

	val currentUserId: String?
		get() = authSource.currentUserId

	fun signOut() {
		authSource.signOut()
	}

	suspend fun login(
		email: String,
		password: String,
		isVendor: Boolean
	): LoginResult {
		val userId = try {
			authSource.login(email, password)
		} catch (exception: Exception) {
			return if (
				exception is FirebaseAuthInvalidCredentialsException ||
				exception is FirebaseAuthInvalidUserException
			) {
				LoginResult.InvalidCredentials
			} else {
				LoginResult.Error
			}
		}

		return try {
			val customer = customerRemote.getCustomerById(userId)
			val vendor = vendorRemote.getVendorById(userId)

			val actualRole = when {
				customer != null && vendor == null ->
					UserRole.CUSTOMER

				vendor != null && customer == null ->
					UserRole.VENDOR

				else -> UserRole.NONE
			}

			val expectedRole = if (isVendor) {
				UserRole.VENDOR
			} else {
				UserRole.CUSTOMER
			}

			if (actualRole == expectedRole) {
				LoginResult.Success(
					userId = userId,
					role = actualRole
				)
			} else {
				authSource.signOut()

				if (actualRole == UserRole.NONE) {
					LoginResult.Error
				} else {
					LoginResult.WrongRole
				}
			}
		} catch (exception: Exception) {
			authSource.signOut()
			LoginResult.Error
		}
	}

	suspend fun register(
		email: String,
		password: String,
		name: String,
		category: String,
		isVendor: Boolean
	): RegisterResult {
		val userId = try {
			authSource.register(email, password)
		} catch (exception: Exception) {
			return when (exception) {
				is FirebaseAuthInvalidCredentialsException ->
					RegisterResult.INVALID_EMAIL

				is FirebaseAuthUserCollisionException ->
					RegisterResult.EMAIL_EXISTS

				is FirebaseAuthWeakPasswordException ->
					RegisterResult.ERROR

				else -> RegisterResult.ERROR
			}
		}

		return try {
			if (isVendor) {
				vendorRemote.addVendor(
					FirebaseVendorData(
						vendorId = userId,
						vendorName = name.trim(),
						vendorEmail = email.trim(),
						rating = 0.0,
						category = category.trim(),
						distance = 0.0,
						moneyBalance = 0.0,
						vendorImage = ""
					)
				)
			} else {
				customerRemote.addCustomer(
					FirebaseCustomerData(
						customerId = userId,
						customerName = name.trim(),
						customerEmail = email.trim(),
						moneyBalance = 0.0,
						customerImage = ""
					)
				)
			}

			RegisterResult.SUCCESS
		} catch (exception: Exception) {
			// Authentication succeeded, but profile creation failed.
			// Attempt to remove the incomplete account.
			try {
				authSource.deleteCurrentUser()
			} catch (_: Exception) {
				authSource.signOut()
			}

			RegisterResult.ERROR
		}
	}

	suspend fun getCurrentUserRole(): UserRole {
		val userId = authSource.currentUserId
			?: return UserRole.NONE

		return try {
			val customer = customerRemote.getCustomerById(userId)
			val vendor = vendorRemote.getVendorById(userId)

			when {
				customer != null && vendor == null ->
					UserRole.CUSTOMER

				vendor != null && customer == null ->
					UserRole.VENDOR

				else -> UserRole.NONE
			}
		} catch (exception: Exception) {
			UserRole.NONE
		}
	}
}