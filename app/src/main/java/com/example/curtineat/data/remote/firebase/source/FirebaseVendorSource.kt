package com.example.curtineat.data.remote.firebase.source

import com.example.curtineat.data.remote.firebase.FirebaseProvider
import com.example.curtineat.data.remote.firebase.model.FirebaseVendorData
import kotlinx.coroutines.tasks.await

class FirebaseVendorSource {

	private val firestore = FirebaseProvider.firestore

	private val vendorCollection =
		firestore.collection("vendors")

	suspend fun getAllVendors(): List<FirebaseVendorData> {

		val snapshot = vendorCollection
			.get()
			.await()

		return snapshot.documents.map { document ->
			FirebaseVendorData(
				vendorId = document.id,
				vendorName = document.getString("vendorName") ?: "",
				vendorEmail = document.getString("vendorEmail") ?: "",
				rating = document.getDouble("rating") ?: 0.0,
				category = document.getString("category") ?: "",
				distance = document.getDouble("distance") ?: 0.0,
				moneyBalance = document.getDouble("moneyBalance") ?: 0.0,
//				isOpen = document.getBoolean("isOpen") ?:true,

			)
		}
	}

	suspend fun getVendorById(
		vendorId: String
	): FirebaseVendorData? {

		val document = vendorCollection
			.document(vendorId)
			.get()
			.await()

		if (!document.exists()) {
			return null
		}

		return FirebaseVendorData(
			vendorId = document.id,
			vendorName = document.getString("vendorName") ?: "",
			vendorEmail = document.getString("vendorEmail") ?: "",
			rating = document.getDouble("rating") ?: 0.0,
			category = document.getString("category") ?: "",
			distance = document.getDouble("distance") ?: 0.0,
			moneyBalance = document.getDouble("moneyBalance") ?: 0.0,
//			isOpen = document.getBoolean("isOpen") ?:true,
		)
	}

	suspend fun addVendor(
		vendor: FirebaseVendorData
	) {

		vendorCollection
			.document(vendor.vendorId)
			.set(
				mapOf(
					"vendorName" to vendor.vendorName,
					"vendorEmail" to vendor.vendorEmail,
					"rating" to vendor.rating,
					"category" to vendor.category,
					"distance" to vendor.distance,
					"moneyBalance" to vendor.moneyBalance,
//					"isOpen" to vendor.isOpen
				)
			)
			.await()
	}

	suspend fun updateVendor(
		vendor: FirebaseVendorData
	) {

		vendorCollection
			.document(vendor.vendorId)
			.set(
				mapOf(
					"vendorName" to vendor.vendorName,
					"vendorEmail" to vendor.vendorEmail,
					"rating" to vendor.rating,
					"category" to vendor.category,
					"distance" to vendor.distance,
					"moneyBalance" to vendor.moneyBalance,
//					"isOpen" to vendor.isOpen
				)
			)
			.await()
	}

	// almost same as updateVendor but accept vendor name and category only, future add pic
	suspend fun updateVendorProfile(
		vendorId: String,
		vendorName: String,
		category: String
	) {

		vendorCollection
			.document(vendorId)
			.update(
				mapOf(
					"vendorName" to vendorName,
					"category" to category
				)
			)
			.await()
	}

	suspend fun deleteVendor(
		vendorId: String
	) {

		vendorCollection
			.document(vendorId)
			.delete()
			.await()
	}
}