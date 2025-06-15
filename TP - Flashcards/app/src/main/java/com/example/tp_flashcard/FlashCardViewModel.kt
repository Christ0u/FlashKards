package com.example.tp_flashcard

import androidx.lifecycle.ViewModel
import com.example.tp_flashcard.model.FlashCard
import com.example.tp_flashcard.model.FlashCardCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FlashcardViewModel(categoryId: Int) : ViewModel() {

    // Propriétés privées mutables
    private val _cardsToStudy = MutableStateFlow<List<FlashCard>>(emptyList())
    private val _currentIndex = MutableStateFlow(0)
    private val _isSessionFinished = MutableStateFlow(false)

    // Propriétés publiques en lecture seule
    val cardsToStudy: StateFlow<List<FlashCard>> = _cardsToStudy
    val currentIndex: StateFlow<Int> = _currentIndex
    val isSessionFinished: StateFlow<Boolean> = _isSessionFinished

    init {
        // On charge les cartes de la catégorie passée en paramètre
        _cardsToStudy.value = FlashcardRepository.cards.filter { it.categoryId == categoryId }
    }

    // Passe à la carte suivante
    fun nextCard(currentIndex: Int) {
        if (_cardsToStudy.value.isNotEmpty() && _currentIndex.value < _cardsToStudy.value.lastIndex) {
            _currentIndex.value += 1
        } else {
            _isSessionFinished.value = true
        }
    }
}