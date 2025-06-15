package com.example.tp_flashcard

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ProgressBar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tp_flashcard.model.FlashCard
import com.example.tp_flashcard.model.FlashCardCategory
import kotlinx.coroutines.flow.forEach

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun FlashcardNavHost(
    modifier: Modifier = Modifier,
    homeViewModel : HomeViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            HomeScreen (homeViewModel = homeViewModel,
                        onCategoryClick = { category: FlashCardCategory ->
                            navController.navigate("revision/${category.id}")
                        })
        }
        composable("revision/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull() ?: 0
            val flashcardViewModel = remember { FlashcardViewModel(categoryId) }

            val cardsToStudy = flashcardViewModel.cardsToStudy.collectAsState()
            val currentIndex = flashcardViewModel.currentIndex.collectAsState()
            val isSessionFinished = flashcardViewModel.isSessionFinished.collectAsState()

            val uiState = FlashCardUiState(
                index = currentIndex.value,
                cardsToStudy = cardsToStudy.value,
                isSessionFinished = isSessionFinished.value
            )

            FlashcardScreen(
                uiState = uiState,
                onNext = { flashcardViewModel.nextCard(currentIndex.value) }
            )
        }
    }
}


@Composable
fun HomeScreen(homeViewModel: HomeViewModel = HomeViewModel(),
               onCategoryClick: (FlashCardCategory) -> Unit) {
    val categories = homeViewModel.categories.collectAsState()

    Column {
        categories.value.forEach { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp) // marges autour de la carte
                    .clickable { onCategoryClick(category) },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // ombre portée
                shape = RoundedCornerShape(16.dp) // coins arrondis
            ) {
                Text(
                    text = category.name,
                    modifier = Modifier.padding(24.dp) // padding interne
                )
            }
        }
    }
}

@Composable
fun FlashcardScreen(
    uiState: FlashCardUiState,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProgressBar(
            current = uiState.index + 1,
            total = uiState.cardsToStudy.size
        )
        Spacer(modifier = Modifier.height(32.dp))
        FlashcardQuestion(
            question = uiState.cardsToStudy.getOrNull(uiState.index)?.question ?: "Aucune question"
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onNext,
            enabled = !uiState.isSessionFinished
        ) {
            Text("Suivant")
        }
    }
}

@Composable
fun ProgressBar(current: Int, total: Int) {
    LinearProgressIndicator(
        progress = { if (total > 0) current / total.toFloat() else 0f },
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp),
    )
    //Text("$current / $total", modifier = Modifier.align(Alignment.CenterHorizontally))
}

@Composable
fun FlashcardQuestion(question: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(24.dp)
        )
    }
}