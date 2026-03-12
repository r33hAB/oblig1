package com.example.quizapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for one gallery item.
 *
 * This is based on the existing BildeOppforing model, but Room stores
 * the image reference as a String instead of Uri.
 */
@Entity(tableName = "gallery_items")
data class GalleryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val uri: String
)