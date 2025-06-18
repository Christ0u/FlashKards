package com.example.flashkards

import com.example.flashkards.model.FlashCard

// Représente l'état de l'interface utilisateur pour une session d'étude de flashcards
data class FlashCardUiState(
    // Index de la carte actuellement affichée
    val index: Int = 0,
    // Liste des cartes à étudier dans la session
    val cardsToStudy: List<FlashCard> = emptyList(),
    // Indique si la session d'étude est terminée
    val isSessionFinished: Boolean = false
)