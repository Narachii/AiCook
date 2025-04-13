package org.example.project.networks

import kotlinx.serialization.Serializable
import org.example.project.entity.Recipe

@Serializable
data class RecipeResponse(
    val ingredients: List<String>,
    val recipe: Recipe,
)