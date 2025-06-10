package com.example.tp_flashcard.model

// Represents a flashcard with a question and its answer
data class FlashCard(
    val id: Int,
    val categoryId: Int,
    val question: String,
    val answer: String
)