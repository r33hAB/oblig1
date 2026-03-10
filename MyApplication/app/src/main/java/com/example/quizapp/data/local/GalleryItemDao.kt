package com.example.quizapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO skeleton for gallery items.
 *
 * The team can expand this interface with update/search methods later.
 *
 * TODO (required): Keep the minimum methods needed by Oblig 2.
 * TODO (optional): Consider whether getAll() should later return Flow<List<GalleryItemEntity>>
 * instead of List so the UI updates automatically.
 * TODO (optional): Consider whether sorting A-Å / Å-A should be handled in DAO queries.
 */
@Dao
interface GalleryItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GalleryItemEntity)

    @Delete
    suspend fun delete(item: GalleryItemEntity)

    @Query("SELECT * FROM gallery_items ORDER BY name ASC")
    suspend fun getAll(): List<GalleryItemEntity>
}
