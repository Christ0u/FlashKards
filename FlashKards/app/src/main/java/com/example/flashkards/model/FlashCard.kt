package com.example.flashkards.model

// Représente une carte de révision avec une question et sa réponse
data class FlashCard(
    val id: Int,
    val categoryId: Int,
    val question: String,
    val answer: String
)