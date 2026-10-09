package com.example.curtineat.data.remote.firebase.source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthSource(
	private val auth: FirebaseAuth
) {

	val currentUser: FirebaseUser?
		get() = auth.currentUser

	val currentUserId: String?
		get() = auth.currentUser?.uid

	suspend fun login(
		email: String,
		password: String
	): String {
		val result = auth.signInWithEmailAndPassword(
			email.trim(),
			password
		).awaitResult()

		return result.user?.uid
			?: throw IllegalStateException(
				"Firebase Authentication returned no user"
			)
	}

	suspend fun register(
		email: String,
		password: String
	): String {
		val result = auth.createUserWithEmailAndPassword(
			email.trim(),
			password
		).awaitResult()

		return result.user?.uid
			?: throw IllegalStateException(
				"Firebase Authentication returned no user"
			)
	}

	suspend fun deleteCurrentUser() {
		auth.currentUser?.delete()?.awaitResult()
	}

	fun signOut() {
		auth.signOut()
	}

	private suspend fun <T> com.google.android.gms.tasks.Task<T>.awaitResult(): T =
		suspendCancellableCoroutine { continuation ->
			addOnCompleteListener { task ->
				if (!continuation.isActive) {
					return@addOnCompleteListener
				}

				if (task.isSuccessful) {
					@Suppress("UNCHECKED_CAST")
					continuation.resume(task.result as T)
				} else {
					continuation.resumeWithException(
						task.exception
							?: IllegalStateException(
								"Firebase operation failed"
							)
					)
				}
			}
		}
}