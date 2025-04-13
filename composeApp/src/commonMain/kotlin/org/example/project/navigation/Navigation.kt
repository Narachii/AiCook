package org.example.project.navigation

import kotlinx.serialization.Serializable

@Serializable
data object ApplicationTopRoute

@Serializable
data object CameraPreviewRoute

@Serializable
data class IngredientRoute(
    val ingredients: List<String>
)

// ViewModel is required in this screen
// Dependency injection is required in this screen
@Serializable
data class RecipeRoute(
    val ingredients: List<String>
)
