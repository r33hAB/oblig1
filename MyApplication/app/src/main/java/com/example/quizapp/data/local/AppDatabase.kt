package com.example.quizapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database skeleton for DAT153 Oblig 2.
 *
 * TODO (required): Create the database as a singleton so the app does not open multiple instances.
 * TODO (required): Decide where the singleton should be created, for example Application or a provider.
 * TODO (required): Prepopulate the database with the built-in images from Oblig 1 if the assignment expects them to persist.
 * TODO (optional): Add a Room callback later if prepopulation is easier there.
 */
@Database(
    entities = [GalleryItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun galleryItemDao(): GalleryItemDao
}
