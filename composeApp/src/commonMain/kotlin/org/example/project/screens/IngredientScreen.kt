package org.example.project.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.example.project.navigation.RecipeRoute
import org.example.project.screens.components.IngredientContent

@Composable
fun IngredientScreen(
    modifier: Modifier = Modifier,
    ingredients: List<String>,
    navController: NavHostController,
) {
    IngredientContent(
        modifier,
        ingredients
    ) { navController.navigate(RecipeRoute(ingredients = it)) }
}
