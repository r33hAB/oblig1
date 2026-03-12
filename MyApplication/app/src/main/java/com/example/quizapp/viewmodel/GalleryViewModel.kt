package com.example.quizapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.BildeOppforing
import com.example.quizapp.data.GalleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel skeleton for the gallery screen.
 *
 * This file is intentionally incomplete so the team can implement the
 * required Room and UI state logic in Oblig 2.
 */
class GalleryViewModel(
    private val repository: GalleryRepository
) : ViewModel() {

    /*
     * TODO (required): Load the gallery list from Room through GalleryRepository.
     * TODO (required): Expose observable gallery state to the UI, for example StateFlow or LiveData.
     * TODO (required): Keep gallery state out of the Activity where possible.
     * TODO (required): Handle loading, empty state, and error state for the gallery screen.
     * TODO (required): Support add and delete operations through this ViewModel.
     * TODO (optional): Keep dialog state here if the team wants less UI state inside the Activity.
     * TODO (optional): Keep sorting state here if gallery sorting becomes part of Room/ViewModel flow.
     */

    private val _uiState = MutableStateFlow(GalleryUiState())
    val uiState: StateFlow<GalleryUiState> = _uiState

    private var currentSortOrder: GallerySortOrder = GallerySortOrder.ASC

    init {
        // Start med å observatere room/database med en gang
        observeGallery()
    }

    private fun observeGallery() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val flow = when (currentSortOrder) {
                GallerySortOrder.ASC -> repository.getAllAsc()
                GallerySortOrder.DESC -> repository.getAllDesc()
            }

            flow.collect { list ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        items = list,
                        isEmpty = list.isEmpty(),
                        errorMessage = null,
                        sortOrder = currentSortOrder
                    )
                }
            }
        }
    }

    fun setSortOrder(order: GallerySortOrder) {
        if (order == currentSortOrder) return
        currentSortOrder = order
        // Re-subscribe med ny rekkefølge/order
        observeGallery()
    }

    fun addImage(name: String, uri: String) {
        // TODO (required): Save a new gallery item to Room.
        // TODO (required): Forward the new item through the repository layer.
        viewModelScope.launch {
            try {
                val item = BildeOppforing(
                    id = 0L, // ID er autogenerert i DAO
                    navn = name,
                    bildeUri = Uri.parse(uri)
                )
                repository.insert(item)
                // Flow fra Room vil oppdatere UI automatisk
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Kunne ikke lagre bildet")
                }
            }
        }
    }

    fun deleteImage(item: BildeOppforing) {
        // TODO (required): Delete the gallery item with the given id from Room.
        viewModelScope.launch {
            try {
                repository.delete(item)
                // Flow fra Room vil oppdatere UI automatisk
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Kunne ikke slette bildet")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
