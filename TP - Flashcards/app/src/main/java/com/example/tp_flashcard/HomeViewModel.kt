package com.example.tp_flashcard

import androidx.lifecycle.ViewModel
import com.example.tp_flashcard.model.FlashCardCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    // Propriété privée et mutable, non exposée à l'UI
    private val _categories = MutableStateFlow<List<FlashCardCategory>>(emptyList())

    // Propriété publique, observable et immuable pour l'UI
    val categories: StateFlow<List<FlashCardCategory>> = _categories

    // Bloc d'initialisation exécuté à la création du ViewModel
    init {
        // Chargement immédiat des catégories depuis le repository
        _categories.value = FlashcardRepository.categories
    }
}