package com.example.curtineat.data.local.room.mapper

import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.data.local.room.entity.OrderProductEntity
import com.example.curtineat.data.remote.firebase.model.FirebaseOrderData
import com.example.curtineat.data.remote.firebase.model.FirebaseOrderProductData

fun FirebaseOrderData.toEntity(): OrderEntity {
	return OrderEntity(
		orderId = orderId,
		vendorId = vendorId,
		customerId = customerId,
		totalPrice = totalPrice,
		status = status,
		timestamp = timestamp.toDate().time
	)
}

fun FirebaseOrderData.toProductEntities(): List<OrderProductEntity> {
	return products.map { product ->
		OrderProductEntity(
			orderId = orderId,
			productId = product.productId,
			productName = product.productName,
			productPrice = product.productPrice,
			quantity = product.quantity
		)
	}
}

fun OrderEntity.toFirebaseModel(
	products: List<OrderProductEntity>
): FirebaseOrderData {
	return FirebaseOrderData(
		orderId = orderId,
		vendorId = vendorId,
		customerId = customerId,
		totalPrice = totalPrice,
		status = status,
		products = products.map { product ->
			FirebaseOrderProductData(
				productId = product.productId,
				productName = product.productName,
				productPrice = product.productPrice,
				quantity = product.quantity
			)
		},
		timestamp = com.google.firebase.Timestamp(
			java.util.Date(timestamp)
		)
	)
}