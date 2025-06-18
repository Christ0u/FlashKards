package com.example.flashkards

import androidx.lifecycle.ViewModel
import com.example.flashkards.model.FlashCardCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ViewModel responsable de la gestion des catégories de flashcards
class HomeViewModel : ViewModel() {

    // Propriété privée mutable contenant la liste des catégories (non exposée à l'UI)
    private val _categories = MutableStateFlow<List<FlashCardCategory>>(emptyList())

    // Propriété publique immuable, observable par l'UI pour réagir aux changements
    val categories: StateFlow<List<FlashCardCategory>> = _categories

    // Bloc d'initialisation exécuté lors de la création du ViewModel
    init {
        // Initialise la liste des catégories à partir du repository
        _categories.value = FlashcardRepository.categories
    }
}