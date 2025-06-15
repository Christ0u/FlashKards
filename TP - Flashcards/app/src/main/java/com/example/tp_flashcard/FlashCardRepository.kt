package com.example.tp_flashcard

import com.example.tp_flashcard.model.FlashCard
import com.example.tp_flashcard.model.FlashCardCategory

object FlashcardRepository {
    val categories: List<FlashCardCategory> = listOf(
        FlashCardCategory(id = 1, name = "Capitales"),
        FlashCardCategory(id = 2, name = "Trains et SNCF"),
        FlashCardCategory(id = 3, name = "Informatique"),
        FlashCardCategory(id = 4, name = "Anglais")
    )

    val cards: List<FlashCard> = listOf(
        // Capitales
        FlashCard(1, 1, "Capitale de la France ?", "Paris"),
        FlashCard(2, 1, "Capitale de l’Espagne ?", "Madrid"),
        FlashCard(3, 1, "Capitale de l’Italie ?", "Rome"),
        FlashCard(4, 1, "Capitale de l’Allemagne ?", "Berlin"),
        FlashCard(5, 1, "Capitale du Royaume-Uni ?", "Londres"),
        FlashCard(6, 1, "Capitale du Portugal ?", "Lisbonne"),
        FlashCard(7, 1, "Capitale de la Belgique ?", "Bruxelles"),
        FlashCard(8, 1, "Capitale de la Suisse ?", "Berne"),
        FlashCard(9, 1, "Capitale de la Grèce ?", "Athènes"),
        FlashCard(10, 1, "Capitale de la Suède ?", "Stockholm"),
        FlashCard(11, 1, "Capitale de la Norvège ?", "Oslo"),
        FlashCard(12, 1, "Capitale de la Finlande ?", "Helsinki"),
        FlashCard(13, 1, "Capitale de la Hongrie ?", "Budapest"),
        FlashCard(14, 1, "Capitale de la Croatie ?", "Zagreb"),
        FlashCard(15, 1, "Capitale de la Lituanie ?", "Vilnius"),

        // Trains
        FlashCard(16, 2, "Tensions d'alimentation des caténaires du réseau ferré français ?", "1 500 V continu et 25 000 V alternatif"),
        FlashCard(17, 2, "Que signifie TER ?", "Train Express Régional"),
        FlashCard(18, 2, "Quelles sont les couleurs de la livrée Carmillon de la SNCF ?", "Gris, rouge et blanc"),
        FlashCard(19, 2, "Qu'est-ce qu'un train bimode ?", "Un train pouvant rouler sur 2 types d'énergie : électrique et diesel"),
        FlashCard(20, 2, "Qu'est-ce qu'un train bicourant ?", "Un train pouvant rouler sur 2 tensions électriques différentes"),
        FlashCard(21, 2, "Comment s'appelle la nouvelle série de TGV développée par Alstom ?", "TGV M"),
        FlashCard(22, 2, "Quel est le nom du train permettant de traverser la manche?", "Eurostar e320"),
        FlashCard(23, 2, "Quelle est la longueur d'un TGV ?", "200 mètres"),
        FlashCard(24, 2, "Quelle est la vitesse record du monde sur rail atteinte par un TGV ?", "574.8 km/h"),
        FlashCard(25, 2, "Qu'est-ce qu'un bogie ?", "Châssis supportant les roues sous un train"),

        // Informatique
        FlashCard(26, 3, "Qui est le créateur de Linux ?", "Linus Torvalds"),
        FlashCard(27, 3, "Que signifie HTML ?", "HyperText Markup Language"),
        FlashCard(28, 3, "Langage principal pour Android ?", "Kotlin"),
        FlashCard(29, 3, "Que veut dire CPU ?", "Central Processing Unit"),
        FlashCard(30, 3, "Navigateur web de Google ?", "Chrome"),
        FlashCard(31, 3, "Système d'exploitation d'Apple pour ordinateurs ?", "macOS"),
        FlashCard(32, 3, "Que signifie RAM ?", "Random Access Memory"),
        FlashCard(33, 3, "Langage créé par Guido van Rossum ?", "Python"),
        FlashCard(34, 3, "Protocole pour envoyer des emails ?", "SMTP"),
        FlashCard(35, 3, "Nom du réseau mondial reliant les ordinateurs ?", "Internet"),

        // Anglais
        FlashCard(36, 4, "Horloge", "Clock"),
        FlashCard(37, 4, "Fauteuil", "Armchair"),
        FlashCard(38, 4, "Papillon", "Butterfly"),
        FlashCard(39, 4, "Clé", "Key"),
        FlashCard(40, 4, "Brouillard", "Fog"),
        FlashCard(41, 4, "Écureuil", "Squirrel"),
        FlashCard(42, 4, "Plafond", "Ceiling"),
        FlashCard(43, 4, "Boussole", "Compass"),
        FlashCard(44, 4, "Cerf-volant", "Kite"),
        FlashCard(45, 4, "Chandelle", "Candle"),
        FlashCard(46, 4, "Témoin (au mariage)", "Witness"),
        FlashCard(47, 4, "Poubelle", "Trash can"),
        FlashCard(48, 4, "Parapluie", "Umbrella"),
        FlashCard(49, 4, "Baleine", "Whale"),
        FlashCard(50, 4, "Chiffre d'affaires", "Turnover")
    )
}