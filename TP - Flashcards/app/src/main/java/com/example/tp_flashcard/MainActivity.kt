package com.example.tp_flashcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class MainActivity : ComponentActivity () {
    private val homeViewModel : HomeViewModel by viewModels ()
    override fun onCreate ( savedInstanceState : Bundle ?) {
        super . onCreate ( savedInstanceState )
        setContent {
            FlashcardNavHost ( homeViewModel = homeViewModel )
        }
    }
}