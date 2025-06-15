package com.example.tp_flashcard

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ProgressBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
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
                onNext = { flashcardViewModel.nextCard(currentIndex.value) },
                navController = navController
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
    onNext: () -> Unit,
    navController: NavController
) {
    var flipped = remember(uiState.index) { mutableStateOf(false) }

    LaunchedEffect(uiState.isSessionFinished) {
        if (uiState.isSessionFinished) {
            navController.popBackStack("home", inclusive = false)
        }
    }

    val rotation = animateFloatAsState(
        targetValue = if (flipped.value) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "flip"
    )
    val isBack = rotation.value > 90f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6FE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            ProgressBar(
                current = uiState.index + 1,
                total = uiState.cardsToStudy.size
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .graphicsLayer {
                        rotationY = rotation.value
                        cameraDistance = 16 * density
                    },
                contentAlignment = Alignment.Center
            ) {
                FlashcardQuestion(
                    question = if (!isBack)
                        uiState.cardsToStudy.getOrNull(uiState.index)?.question ?: "Aucune question"
                    else
                        uiState.cardsToStudy.getOrNull(uiState.index)?.answer ?: "Aucune réponse",
                    // On inverse le texte au dos pour l'effet flip
                    modifier = if (isBack) Modifier.graphicsLayer { rotationY = 180f } else Modifier,
                    onClick = { flipped.value = !flipped.value }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    onNext()
                    flipped.value = false // Réinitialise la carte pour la suivante
                },
                enabled = !uiState.isSessionFinished,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
            ) {
                Text("Suivant", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProgressBar(current: Int, total: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LinearProgressIndicator(
            progress = { if (total > 0) current / total.toFloat() else 0f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Color(0xFF6C63FF),
            trackColor = Color(0xFFE0E0E0)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "$current / $total",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6C63FF)
        )
    }
}

@Composable
fun FlashcardQuestion(
    question: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(16.dp)
            .clickable(
                indication = null, // Désactive le grisage/ripple
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = question,
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF333366),
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}