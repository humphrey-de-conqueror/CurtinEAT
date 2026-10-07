package com.example.curtineat.data.local.room.mapper

import com.example.curtineat.data.local.room.entity.ProductEntity
import com.example.curtineat.data.remote.firebase.model.FirebaseProductData

fun FirebaseProductData.toEntity(): ProductEntity {

	return ProductEntity(
		productId = productId,
		vendorId = vendorId,
		productName = productName,
		productPrice = productPrice,
		productImage = productImage,
		isAvailable = isAvailable
	)
}

fun ProductEntity.toFirebaseData(): FirebaseProductData {

	return FirebaseProductData(
		productId = productId,
		vendorId = vendorId,
		productName = productName,
		productPrice = productPrice,
		productImage = productImage,
		isAvailable = isAvailable
	)
}