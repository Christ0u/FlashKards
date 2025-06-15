package com.example.flashkards

import com.example.flashkards.model.FlashCard

data class FlashCardUiState (
    val index: Int = 0,
    val cardsToStudy: List<FlashCard> = emptyList<FlashCard>(),
    val isSessionFinished: Boolean = false
)