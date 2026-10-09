package com.example.curtineat.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_images")
data class CachedImageEntity(
	// Image ID returned by your existing image API.
	@PrimaryKey
	val imageId: String,

	// Raw image bytes stored as a SQLite BLOB.
	val imageBytes: ByteArray,

	// Unix timestamp in milliseconds when cached.
	val cachedAt: Long
)