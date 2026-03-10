package com.example.quizapp.data

import com.example.quizapp.data.local.GalleryItemDao

/**
 * Optional repository layer between Room and ViewModels.
 *
 * Keep this class small and student-friendly. The full logic can be added
 * incrementally by the team during Oblig 2.
 */
class GalleryRepository(
    private val galleryItemDao: GalleryItemDao
) {
    /*
     * TODO (required): Expose a method for loading gallery items from Room.
     * TODO (required): Map GalleryItemEntity objects to BildeOppforing and back.
     * TODO (required): Decide where Uri.parse(...) and Uri.toString() should live.
     * TODO (required): Add a method for inserting a gallery item.
     * TODO (required): Add a method for deleting a gallery item.
     * TODO (optional): Decide whether QuizApplication should use this repository later.
     * TODO (optional): Keep mapping code here if the team wants Activities and ViewModels to stay simpler.
     */
}
