package com.example.quizapp.viewmodel

import androidx.lifecycle.ViewModel

/**
 * ViewModel skeleton for the gallery screen.
 *
 * This file is intentionally incomplete so the team can implement the
 * required Room and UI state logic in Oblig 2.
 */
class GalleryViewModel : ViewModel() {

    /*
     * TODO (required): Load the gallery list from Room through GalleryRepository.
     * TODO (required): Expose observable gallery state to the UI, for example StateFlow or LiveData.
     * TODO (required): Keep gallery state out of the Activity where possible.
     * TODO (required): Handle loading, empty state, and error state for the gallery screen.
     * TODO (required): Support add and delete operations through this ViewModel.
     * TODO (optional): Keep dialog state here if the team wants less UI state inside the Activity.
     * TODO (optional): Keep sorting state here if gallery sorting becomes part of Room/ViewModel flow.
     */

    fun addImage(name: String, uri: String) {
        // TODO (required): Save a new gallery item to Room.
        // TODO (required): Forward the new item through the repository layer.
    }

    fun deleteImage(itemId: Int) {
        // TODO (required): Delete the gallery item with the given id from Room.
    }
}
