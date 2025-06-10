package com.example.tp_flashcard

import com.example.tp_flashcard.model.FlashCard

data class FlashCardUiState (
    val index: Int = 0,
    val cardsToStudy: List<FlashCard> = emptyList<FlashCard>(),
    val isSessionFinished: Boolean = false
)