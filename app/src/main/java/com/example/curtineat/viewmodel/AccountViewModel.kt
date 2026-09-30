package com.example.curtineat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    var username by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var passwordCheck by mutableStateOf("")
        private set

    var selectedGender by mutableStateOf("")
        private set

    var dob by mutableStateOf("")
        private set


    fun updateUsername(value: String) {
        username = value
    }

    fun updateEmail(value: String) {
        email = value
    }

    fun updatePassword(value: String) {
        password = value
    }

    fun updatePasswordCheck(value: String) {
        passwordCheck = value
    }

    fun updateGender(value: String) {
        selectedGender = value
    }

    fun updateDob(value: String) {
        dob = value
    }

    fun Login() {
        // Login logic
    }

    fun CreateAccount() {
        // Create account logic
    }
}