package org.example.project.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.example.project.RecipeViewModel
import org.example.project.screens.ApplicationTopScreen
import org.example.project.screens.IngredientScreen
import org.example.project.screens.RecipeScreen
import org.example.project.shared.CameraPreviewScreen

@Composable
fun createApplicationNavGraph(navController: NavHostController, viewModel: RecipeViewModel) {
    NavHost(
        navController = navController,
        startDestination = ApplicationTopRoute,
    ) {
        applicationRoute(navController)
        cameraPreviewRoute(navController)
        ingredientRoute(navController)
        recipeRoute(navController, viewModel)
    }
}

private fun NavGraphBuilder.applicationRoute(navController: NavHostController) {
    composable<ApplicationTopRoute> {
        ApplicationTopScreen(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
        )
    }
}

private fun NavGraphBuilder.cameraPreviewRoute(navController: NavHostController) {
    composable<CameraPreviewRoute> {
        CameraPreviewScreen(
            modifier = Modifier.fillMaxSize(),
            navHostController = navController,
        )
    }
}

private fun NavGraphBuilder.ingredientRoute(navController: NavHostController) {
    composable<IngredientRoute> {
        val ingredients = it.toRoute<IngredientRoute>().ingredients
        IngredientScreen(
            modifier = Modifier.fillMaxSize(),
            ingredients = ingredients,
            navController = navController,
        )
    }
}

private fun NavGraphBuilder.recipeRoute(
    navController: NavHostController,
    viewModel: RecipeViewModel
) {
    composable<RecipeRoute> {
        val ingredients = it.toRoute<RecipeRoute>().ingredients
        RecipeScreen(
            modifier = Modifier.fillMaxSize(),
            ingredientList = ingredients,
            viewModel = viewModel,
            navController = navController,
        )
    }
}
