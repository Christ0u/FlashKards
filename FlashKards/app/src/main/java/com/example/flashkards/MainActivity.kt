package com.example.flashkards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.flashkards.ui.theme.FlashKards

// Classe principale de l'application, héritant de ComponentActivity
class MainActivity : ComponentActivity() {
    // Instancie le ViewModel pour gérer les données de l'écran d'accueil
    private val homeViewModel: HomeViewModel by viewModels()

    // Méthode appelée lors de la création de l'activité
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Définit le contenu de l'interface utilisateur avec Jetpack Compose
        setContent {
            FlashKards {
                // Initialise la navigation principale en passant le ViewModel
                FlashcardNavHost(homeViewModel = homeViewModel)
            }
        }
    }
}