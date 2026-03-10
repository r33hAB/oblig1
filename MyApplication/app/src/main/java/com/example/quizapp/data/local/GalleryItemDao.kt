package com.example.quizapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GalleryItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GalleryItemEntity)

    @Delete
    suspend fun delete(item: GalleryItemEntity)

    // Vi velger Flow og ikke List fordi da kan UI automatisk få oppdateringer når databasen endres
    // A-Å
    @Query("SELECT * FROM gallery_items ORDER BY name ASC")
    fun getAllAsc(): Flow<List<GalleryItemEntity>>

    // Å-A
    @Query("SELECT * FROM gallery_items ORDER BY name DESC")
    fun getAllDesc(): Flow<List<GalleryItemEntity>>
}
