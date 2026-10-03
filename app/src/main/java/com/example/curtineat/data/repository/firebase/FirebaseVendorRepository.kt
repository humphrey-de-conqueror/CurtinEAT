package com.example.curtineat.data.repository.firebase

import com.example.curtineat.data.remote.firebase.model.FirebaseVendorData
import com.example.curtineat.data.remote.firebase.source.FirebaseVendorSource

class FirebaseVendorRepository(
	private val source: FirebaseVendorSource
) {

	suspend fun getAllVendors(): List<FirebaseVendorData> {

		return source.getAllVendors()
	}

	suspend fun getVendorById(
		vendorId: String
	): FirebaseVendorData? {

		return source.getVendorById(vendorId)
	}

	suspend fun addVendor(
		vendor: FirebaseVendorData
	) {

		source.addVendor(vendor)
	}

	suspend fun updateVendor(
		vendor: FirebaseVendorData
	) {

		source.updateVendor(vendor)
	}

	suspend fun deleteVendor(
		vendorId: String
	) {

		source.deleteVendor(vendorId)
	}
}