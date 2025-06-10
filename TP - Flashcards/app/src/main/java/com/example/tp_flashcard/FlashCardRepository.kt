package com.example.tp_flashcard

import com.example.tp_flashcard.model.FlashCard
import com.example.tp_flashcard.model.FlashCardCategory

object FlashcardRepository {
    val categories: List<FlashCardCategory> = listOf(
        FlashCardCategory(id = 1, name = "Mathématiques"),
        FlashCardCategory(id = 2, name = "Histoire"),
        FlashCardCategory(id = 3, name = "Géographie")
    )

    val cards: List<FlashCard> = listOf(
        // Mathématiques
        FlashCard(id = 1, categoryId = 1, question = "2 + 2 ?", answer = "4"),
        FlashCard(id = 2, categoryId = 1, question = "Racine carrée de 16 ?", answer = "4"),
        FlashCard(id = 3, categoryId = 1, question = "5 × 7 ?", answer = "35"),
        FlashCard(id = 4, categoryId = 1, question = "10 / 2 ?", answer = "5"),
        FlashCard(id = 5, categoryId = 1, question = "3² ?", answer = "9"),
        FlashCard(id = 6, categoryId = 1, question = "Nombre premier après 7 ?", answer = "11"),
        FlashCard(id = 7, categoryId = 1, question = "12 - 5 ?", answer = "7"),
        FlashCard(id = 8, categoryId = 1, question = "Factorielle de 3 ?", answer = "6"),

        // Histoire
        FlashCard(id = 9, categoryId = 2, question = "Qui a découvert l’Amérique ?", answer = "Christophe Colomb"),
        FlashCard(id = 10, categoryId = 2, question = "En quelle année la Révolution française a-t-elle commencé ?", answer = "1789"),
        FlashCard(id = 11, categoryId = 2, question = "Qui était Napoléon ?", answer = "Un empereur français"),
        FlashCard(id = 12, categoryId = 2, question = "Date de la chute de l’Empire romain ?", answer = "476"),
        FlashCard(id = 13, categoryId = 2, question = "Qui était Jeanne d’Arc ?", answer = "Une héroïne française"),
        FlashCard(id = 14, categoryId = 2, question = "Première Guerre mondiale : dates ?", answer = "1914-1918"),
        FlashCard(id = 15, categoryId = 2, question = "Qui a écrit 'Le Siècle des Lumières' ?", answer = "Voltaire"),
        FlashCard(id = 16, categoryId = 2, question = "Qui était Cléopâtre ?", answer = "Reine d’Égypte"),

        // Géographie
        FlashCard(id = 17, categoryId = 3, question = "Capitale de l’Espagne ?", answer = "Madrid"),
        FlashCard(id = 18, categoryId = 3, question = "Plus grand océan du monde ?", answer = "Océan Pacifique"),
        FlashCard(id = 19, categoryId = 3, question = "Pays du Mont Fuji ?", answer = "Japon"),
        FlashCard(id = 20, categoryId = 3, question = "Plus long fleuve du monde ?", answer = "Nil"),
        FlashCard(id = 21, categoryId = 3, question = "Désert le plus vaste ?", answer = "Sahara"),
        FlashCard(id = 22, categoryId = 3, question = "Capitale du Canada ?", answer = "Ottawa"),
        FlashCard(id = 23, categoryId = 3, question = "Combien de continents ?", answer = "7"),
        FlashCard(id = 24, categoryId = 3, question = "Pays avec la plus grande population ?", answer = "Chine")
    )
}