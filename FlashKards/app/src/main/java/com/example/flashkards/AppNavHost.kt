package com.example.flashkards

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flashkards.model.FlashCardCategory
import kotlinx.coroutines.launch
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun FlashcardNavHost(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    // Contrôleur de navigation pour gérer les écrans
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            // Écran d'accueil avec la liste des catégories
            HomeScreen(
                homeViewModel = homeViewModel,
                onCategoryClick = { category: FlashCardCategory ->
                    navController.navigate("revision/${category.id}")
                }
            )
        }
        composable("revision/{categoryId}") { backStackEntry ->
            // Récupère l'id de la catégorie sélectionnée
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull() ?: 0
            // Instancie le ViewModel pour la session de révision
            val flashcardViewModel = remember { FlashcardViewModel(categoryId) }

            // Observe les états nécessaires à l'affichage
            val cardsToStudy = flashcardViewModel.cardsToStudy.collectAsState()
            val currentIndex = flashcardViewModel.currentIndex.collectAsState()
            val isSessionFinished = flashcardViewModel.isSessionFinished.collectAsState()

            // Construit l'état UI à partir des flows du ViewModel
            val uiState = FlashCardUiState(
                index = currentIndex.value,
                cardsToStudy = cardsToStudy.value,
                isSessionFinished = isSessionFinished.value
            )

            // Affiche l'écran de révision
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
    // Observe la liste des catégories depuis le ViewModel
    val categories = homeViewModel.categories.collectAsState()
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var showCredits by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titre principal de l'application
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(top = 32.dp, bottom = 16.dp)
        )

        // Affiche chaque catégorie sous forme de carte cliquable
        categories.value.forEach { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onCategoryClick(category) },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = category.name,
                    modifier = Modifier.padding(24.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Bouton pour quitter l'application (retour arrière)
        Button(
            onClick = { backDispatcher?.onBackPressed() },
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text("Quitter l'application")
        }

        // Bouton pour afficher la boîte de dialogue des crédits
        Button(
            onClick = { showCredits = true },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Crédits")
        }
    }
    // Affiche la boîte de dialogue des crédits si demandé
    if (showCredits) {
        AlertDialog(
            onDismissRequest = { showCredits = false },
            title = { Text("Crédits") },
            text = { Text("Développé par Christopher GERARD\n© 2025 FlashKards") },
            confirmButton = {
                TextButton(onClick = { showCredits = false }) {
                    Text("Fermer")
                }
            }
        )
    }
}

@Composable
fun FlashcardScreen(
    uiState: FlashCardUiState,
    onNext: () -> Unit,
    navController: NavController
) {
    // Si la session est terminée, retourne automatiquement à l'accueil
    LaunchedEffect(uiState.isSessionFinished) {
        if (uiState.isSessionFinished) {
            navController.popBackStack("home", inclusive = false)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            // Affiche la progression de la session
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
                // Affiche la carte animée (question/réponse)
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
            // Bouton pour passer à la carte suivante
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
    // Barre de progression et compteur de cartes
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LinearProgressIndicator(
            progress = { if (total > 0) current / total.toFloat() else 0f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "$current / $total",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun FlashcardFlipCard(
    question: String,
    answer: String
) {
    // Carte animée qui se retourne pour afficher la réponse
    val rotation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current.density

    // Remet la carte côté question à chaque nouvelle question
    LaunchedEffect(question, answer) {
        rotation.snapTo(0f)
    }

    val rot = rotation.value
    val isBack = rot > 90f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .padding(16.dp)
            .graphicsLayer {
                rotationY = rot
                cameraDistance = 8 * density
            }
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                // Lance l'animation de flip au clic
                coroutineScope.launch {
                    val target = if (rotation.value < 90f) 180f else 0f
                    rotation.animateTo(
                        target,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(24.dp)
                )
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            // Face question (visible si la carte n'est pas retournée)
            Text(
                text = question,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(24.dp)
                    .graphicsLayer {
                        alpha = if (!isBack) 1f else 0f
                    }
            )
            // Face réponse (visible si la carte est retournée)
            Text(
                text = answer,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(24.dp)
                    .graphicsLayer {
                        rotationY = 180f
                        alpha = if (isBack) 1f else 0f
                        cameraDistance = 8 * density
                    }
            )
        }
    }
}