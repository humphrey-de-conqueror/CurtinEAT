package com.example.curtineat.data.local.room.mapper

import com.example.curtineat.data.local.room.entity.CustomerEntity
import com.example.curtineat.data.remote.firebase.model.FirebaseCustomerData

fun FirebaseCustomerData.toEntity(): CustomerEntity {
	return CustomerEntity(
		customerId = customerId,
		customerName = customerName,
		customerEmail = customerEmail,
		moneyBalance = moneyBalance,
		customerImage = customerImage
	)
}

fun CustomerEntity.toFirebaseModel(): FirebaseCustomerData {
	return FirebaseCustomerData(
		customerId = customerId,
		customerName = customerName,
		customerEmail = customerEmail,
		moneyBalance = moneyBalance,
		customerImage = customerImage
	)
}