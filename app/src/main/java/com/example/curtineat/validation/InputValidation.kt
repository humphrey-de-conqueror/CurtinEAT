package com.example.curtineat.validation

import android.util.Patterns

object InputValidation {

    // Check whether the email follows a valid email pattern
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Check whether both passwords are identical
    fun passwordsMatch(
        password: String,
        confirmPassword: String
    ): Boolean {
        return password == confirmPassword
    }

    // Check whether the password is not empty
    fun isPasswordNotEmpty(password: String): Boolean {
        return password.isNotBlank()
    }

    fun areAllFieldsFilled(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        gender: String,
        dob: String
    ): Boolean {
        return username.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                gender.isNotBlank() &&
                dob.isNotBlank()
    }
}