package com.example.quizapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database skeleton for DAT153 Oblig 2.
 */
@Database(
    entities = [GalleryItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun galleryItemDao(): GalleryItemDao
}
