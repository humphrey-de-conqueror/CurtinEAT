package com.example.curtineat.model

import com.example.curtineat.data.remote.firebase.model.FirebaseProductData

data class CartItem(
    val product: FirebaseProductData,
    val quantity: Int = 1
)