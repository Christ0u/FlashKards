package com.example.tp_flashcard

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tp_flashcard.model.FlashCardCategory
import kotlinx.coroutines.launch

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
fun HomeScreen(
    homeViewModel: HomeViewModel = HomeViewModel(),
    onCategoryClick: (FlashCardCategory) -> Unit
) {
    val categories = homeViewModel.categories.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        categories.value.forEach { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable(
                        indication = null, // Désactive le grisage/ripple
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onCategoryClick(category) },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = category.name,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FlashcardScreen(
    uiState: FlashCardUiState,
    onNext: () -> Unit,
    navController: NavController
) {
    // Retour à l'accueil si session terminée
    LaunchedEffect(uiState.isSessionFinished) {
        if (uiState.isSessionFinished) {
            navController.popBackStack("home", inclusive = false)
        }
    }

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
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = uiState.index,
                    transitionSpec = {
                        (slideInHorizontally { width -> width } + fadeIn()) togetherWith
                                (slideOutHorizontally { width -> -width } + fadeOut())
                    }
                ) { index ->
                    FlashcardFlipCard(
                        question = uiState.cardsToStudy.getOrNull(index)?.question ?: "Aucune question",
                        answer = uiState.cardsToStudy.getOrNull(index)?.answer ?: "Aucune réponse"
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onNext() },
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
fun FlashcardFlipCard(
    question: String,
    answer: String
) {
    val rotation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current.density

    // Réinitialise la rotation à chaque recomposition (donc à chaque nouvelle carte)
    LaunchedEffect(question, answer) {
        rotation.snapTo(0f)
    }

    val rot = rotation.value
    val isBack = rot > 90f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp) // Hauteur augmentée
            .padding(16.dp)
            .graphicsLayer {
                rotationY = rot
                cameraDistance = 8 * density
            }
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                coroutineScope.launch {
                    val target = if (rotation.value < 90f) 180f else 0f
                    rotation.animateTo(
                        target,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Permet le scroll si besoin
            contentAlignment = Alignment.Center
        ) {
            if (!isBack) {
                Text(
                    text = question,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF333366),
                    modifier = Modifier.padding(24.dp)
                )
            } else {
                Text(
                    text = answer,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF6C63FF),
                    modifier = Modifier
                        .padding(24.dp)
                        .graphicsLayer { rotationY = 180f } // Pour remettre le texte à l'endroit
                )
            }
        }
    }
}