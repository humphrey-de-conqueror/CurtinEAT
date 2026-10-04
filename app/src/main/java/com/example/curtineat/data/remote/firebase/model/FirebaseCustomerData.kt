package com.example.curtineat.data.remote.firebase.model

data class FirebaseCustomerData(
	val customerId: String          = "",
	val customerName: String        = "",
	val customerEmail: String       = "",
	val moneyBalance: Double     = 0.0 ,
	//Maybe add profile picture
)