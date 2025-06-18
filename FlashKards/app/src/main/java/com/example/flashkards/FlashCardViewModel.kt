package com.example.flashkards

import androidx.lifecycle.ViewModel
import com.example.flashkards.model.FlashCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ViewModel gérant l'état d'une session d'étude de flashcards pour une catégorie donnée
class FlashcardViewModel(categoryId: Int) : ViewModel() {

    // Liste des cartes à étudier (privée et mutable)
    private val _cardsToStudy = MutableStateFlow<List<FlashCard>>(emptyList())
    // Index de la carte actuellement affichée (privé et mutable)
    private val _currentIndex = MutableStateFlow(0)
    // Indique si la session d'étude est terminée (privé et mutable)
    private val _isSessionFinished = MutableStateFlow(false)

    // Exposition en lecture seule des propriétés à l'UI
    val cardsToStudy: StateFlow<List<FlashCard>> = _cardsToStudy
    val currentIndex: StateFlow<Int> = _currentIndex
    val isSessionFinished: StateFlow<Boolean> = _isSessionFinished

    init {
        // Chargement des cartes correspondant à la catégorie sélectionnée
        _cardsToStudy.value = FlashcardRepository.cards.filter { it.categoryId == categoryId }
    }

    // Passe à la carte suivante ou termine la session si c'est la dernière
    fun nextCard(currentIndex: Int) {
        if (_cardsToStudy.value.isNotEmpty() && _currentIndex.value < _cardsToStudy.value.lastIndex) {
            _currentIndex.value += 1
        } else {
            _isSessionFinished.value = true
        }
    }
}