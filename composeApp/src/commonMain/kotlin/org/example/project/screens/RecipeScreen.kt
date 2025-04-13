package org.example.project.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.example.project.RecipeViewModel
import org.example.project.entity.Result
import org.example.project.navigation.ApplicationTopRoute
import org.example.project.screens.components.RecipeContent

@Composable
fun RecipeScreen(
    modifier: Modifier = Modifier,
    ingredientList: List<String>,
    viewModel: RecipeViewModel,
    navController: NavHostController,
) {
    val screenState = viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(false) {
        viewModel.askRecipe(ingredientList)
    }

    when (screenState.value) {
        is Result.Success -> {
            val recipeData = (screenState.value as Result.Success).data
            RecipeContent(
                modifier = modifier,
                recipe = recipeData,
                onClick = { navController.navigate(ApplicationTopRoute) }
            )
        }

        is Result.Error -> {

        }

        is Result.Loading -> {
            LoadingScreen(
                modifier,
                "Finding Recipe"
            )
        }
    }
}