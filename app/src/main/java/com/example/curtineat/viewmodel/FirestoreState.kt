package com.example.curtineat.viewmodel

import com.example.curtineat.database.Product

sealed class FirestoreState {

    object Idle : FirestoreState()

    object Loading : FirestoreState()

    data class Success(
        val products: List<Product>
    ) : FirestoreState()

    object Empty : FirestoreState()

    data class Error(
        val message: String
    ) : FirestoreState()
}