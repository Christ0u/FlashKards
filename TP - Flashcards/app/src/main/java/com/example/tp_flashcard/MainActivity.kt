package com.example.tp_flashcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.tp_flashcard.ui.theme.TP_FlashcardTheme

class MainActivity : ComponentActivity () {
    private val homeViewModel : HomeViewModel by viewModels ()
    override fun onCreate ( savedInstanceState : Bundle ?) {
        super . onCreate ( savedInstanceState )
        setContent {
            TP_FlashcardTheme {
                FlashcardNavHost ( homeViewModel = homeViewModel )
            }
        }
    }
}