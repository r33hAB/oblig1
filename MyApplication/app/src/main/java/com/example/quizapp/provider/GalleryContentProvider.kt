package com.example.quizapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 * ContentProvider skeleton for exposing gallery metadata.
 *
 * This provider is prepared to expose at least name and uri fields.
 * Full provider logic should be added later with Room integration.
 *
 * TODO (required): Define the provider columns clearly, at minimum name and uri.
 * TODO (required): Define URI patterns for collection vs single item access.
 * TODO (required): Make the provider easy to test with adb shell content query.
 * TODO (required): Decide how Room is accessed from the provider, directly or through a repository.
 * TODO (optional): Add a small contract object later if the team wants cleaner constants.
 */
class GalleryContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.quizapp.provider.gallery"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/gallery_items")
        // TODO (required): Add constants for column names and URI matcher paths.
    }

    override fun onCreate(): Boolean {
        // TODO (required): Initialize the dependencies needed by the provider.
        // TODO (required): Connect the provider to AppDatabase or GalleryRepository.
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        // TODO (required): Query Room and return gallery metadata such as name and uri.
        // TODO (required): Handle both collection URI and single item URI patterns.
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // TODO (required): Insert a new gallery metadata row through Room.
        return null
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        // TODO (required): Update an existing gallery metadata row.
        return 0
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        // TODO (required): Delete a gallery metadata row.
        return 0
    }

    override fun getType(uri: Uri): String? {
        // TODO (required): Return the correct MIME type for collection vs single item URIs.
        return null
    }
}
