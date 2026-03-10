package com.example.quizapp.viewmodel

import com.example.quizapp.data.BildeOppforing

enum class GallerySortOrder { ASC, DESC }

data class GalleryUiState (
    val isLoading: Boolean = true,
    val items: List<BildeOppforing> = emptyList(),
    val isEmpty: Boolean = false,
    val errorMessage: String? = null,
    val sortOrder: GallerySortOrder = GallerySortOrder.ASC
)