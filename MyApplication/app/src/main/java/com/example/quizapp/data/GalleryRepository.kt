package com.example.quizapp.data

import com.example.quizapp.data.local.GalleryItemDao
import com.example.quizapp.data.local.GalleryItemEntity
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GalleryRepository(
    private val galleryItemDao: GalleryItemDao
) {

    // Leser fra Room og konverterer fra GalleryItemEntity → BildeOppforing
    private fun entityToBildeOppforing(entity: GalleryItemEntity): BildeOppforing {
        return BildeOppforing(
            id = entity.id,
            navn = entity.name,
            bildeUri = Uri.parse(entity.uri)
        )
    }

    // Her lagres det i Room som konverteres fra BildeOppforing → GalleryItemEntity
    private fun bildeOppforingToEntity(item: BildeOppforing): GalleryItemEntity {
        return GalleryItemEntity(
            id = 0,
            name = item.navn,
            uri = item.bildeUri.toString()
        )
    }

    fun getAllAsc(): Flow<List<BildeOppforing>> {
        return galleryItemDao.getAllAsc().map { list ->
            list.map { entityToBildeOppforing(it) }
        }
    }

    fun getAllDesc(): Flow<List<BildeOppforing>> {
        return galleryItemDao.getAllDesc().map { list ->
            list.map { entityToBildeOppforing(it) }
        }
    }

    suspend fun insert(item: BildeOppforing) {
        galleryItemDao.insert(bildeOppforingToEntity(item))
    }

    suspend fun delete(item: BildeOppforing) {
        galleryItemDao.delete(bildeOppforingToEntity(item))
    }
}
