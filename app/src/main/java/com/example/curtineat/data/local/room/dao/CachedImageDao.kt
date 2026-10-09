package com.example.curtineat.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.curtineat.data.local.room.entity.CachedImageEntity

@Dao
interface CachedImageDao {

	@Query("SELECT * FROM cached_images WHERE imageId = :imageId LIMIT 1")
	suspend fun getById(imageId: String): CachedImageEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(image: CachedImageEntity)

	@Query("DELETE FROM cached_images WHERE imageId = :imageId")
	suspend fun deleteById(imageId: String)

	@Query("DELETE FROM cached_images")
	suspend fun deleteAll()

	@Query("SELECT COALESCE(SUM(LENGTH(imageBytes)), 0) FROM cached_images")
	suspend fun getTotalBytes(): Long

	@Query("""
        DELETE FROM cached_images
        WHERE imageId IN (
            SELECT imageId FROM cached_images
            ORDER BY cachedAt ASC
            LIMIT :count
        )
    """)
	suspend fun deleteOldest(count: Int)
}