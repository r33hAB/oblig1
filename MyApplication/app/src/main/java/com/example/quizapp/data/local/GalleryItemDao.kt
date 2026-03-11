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
    suspend fun insert(item: GalleryItemEntity): Long

    @Delete
    suspend fun delete(item: GalleryItemEntity)

    // Vi velger Flow og ikke List fordi da kan UI automatisk få oppdateringer når databasen endres
    // A-Å
    @Query("SELECT * FROM gallery_items ORDER BY name ASC")
    fun getAllAsc(): Flow<List<GalleryItemEntity>>

    // Å-A
    @Query("SELECT * FROM gallery_items ORDER BY name DESC")
    fun getAllDesc(): Flow<List<GalleryItemEntity>>

    @Query("SELECT * FROM gallery_items ORDER BY name ASC")
    fun getAllSync(): List<GalleryItemEntity>

    @Query("SELECT * FROM gallery_items WHERE id = :id")
    fun getById(id: Long): GalleryItemEntity?

    @Query("DELETE FROM gallery_items WHERE id = :id")
    fun deleteById(id: Long): Int

    @Query("UPDATE gallery_items SET name = :name, uri = :uri WHERE id = :id")
    fun updateById(id: Long, name: String, uri: String): Int
}
