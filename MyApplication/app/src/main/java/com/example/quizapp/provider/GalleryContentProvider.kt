package com.example.quizapp.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.opengl.Matrix
import com.example.quizapp.QuizApplication
import com.example.quizapp.data.local.GalleryItemEntity
import kotlinx.coroutines.runBlocking

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
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_URI = "URI"

        private const val CODE_GALLERY_ITEMS = 1
        private const val CODE_GALLERY_ITEMS_ID = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "gallery_items", CODE_GALLERY_ITEMS)
            addURI(AUTHORITY, "gallery_items/#", CODE_GALLERY_ITEMS_ID)
        }

        private const val MIME_DIR = "vnd.android.cursor.dir/vnd.com.example.quizapp.gallery_item"
        private const val MIME_ITEM = "vnd.android.cursor.item/vnd.com.example.quizapp.gallery_item"
    }

    private var dao: com.example.quizapp.data.local.GalleryItemDao? = null

    override fun onCreate(): Boolean {
        // TODO (required): Initialize the dependencies needed by the provider.
        // TODO (required): Connect the provider to AppDatabase or GalleryRepository.
        val ctx = context ?: return false
        val app = ctx.applicationContext as? QuizApplication ?: return false
        dao = app.database.galleryItemDao()
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
        val d = dao ?: return null
        val columns = projection?.takeIf { it.isNotEmpty() } ?: arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_URI)

        when (uriMatcher.match(uri)) {
            CODE_GALLERY_ITEMS -> {
                val list = d.getAllSync()
                val cursor = MatrixCursor(columns)
                for (entity in list) {
                    cursor.addRow(arrayOf(entity.id, entity.name, entity.uri))
                }
                return cursor
            }
            CODE_GALLERY_ITEMS_ID -> {
                val id = ContentUris.parseId(uri)
                val entity = d.getById(id) ?: return MatrixCursor(columns)
                val cursor = MatrixCursor(columns)
                cursor.addRow(arrayOf(entity.id, entity.name, entity.uri))
                return cursor
            }
            else -> return null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // TODO (required): Insert a new gallery metadata row through Room.
        if (uriMatcher.match(uri) != CODE_GALLERY_ITEMS) return null
        val d = dao ?: return null
        val name = values?.getAsString(COLUMN_NAME) ?: return null
        val uriStr = values.getAsString(COLUMN_URI) ?: return null
        val entity = GalleryItemEntity(name = name, uri = uriStr)
        val id = runBlocking { d.insert(entity) }
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        // TODO (required): Update an existing gallery metadata row.
        if (uriMatcher.match(uri) != CODE_GALLERY_ITEMS_ID) return 0
        val d = dao ?: return 0
        val id = ContentUris.parseId(uri)
        val name = values?.getAsString(COLUMN_NAME) ?: return 0
        val uriStr = values.getAsString(COLUMN_URI) ?: return 0
        return d.updateById(id, name, uriStr)
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        // TODO (required): Delete a gallery metadata row.
        val d = dao ?: return 0
        return when (uriMatcher.match(uri)) {
            CODE_GALLERY_ITEMS_ID -> d.deleteById(ContentUris.parseId(uri))
            CODE_GALLERY_ITEMS -> 0
            else -> 0
        }
    }

    override fun getType(uri: Uri): String? = when (uriMatcher.match(uri)) {
        // TODO (required): Return the correct MIME type for collection vs single item URIs.
        CODE_GALLERY_ITEMS -> MIME_DIR
        CODE_GALLERY_ITEMS_ID -> MIME_ITEM
        else -> null
    }
}
