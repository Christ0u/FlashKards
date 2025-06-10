package com.example.tp_flashcard

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tp_flashcard.model.FlashCardCategory
import kotlinx.coroutines.flow.forEach

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
                            navController.navigate("revision")
                        })
        }
        composable("revision")
        {
            Text("ECRAN DE REVISION")
            //RevisionScreen( homeViewModel = homeViewModel )
        }
    }
}


@Preview
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
fun RevisionScreen(homeViewModel: HomeViewModel) {

}