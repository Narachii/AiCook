package org.example.project.networks

import kotlinx.serialization.Serializable

@Serializable
data class RecipeRequest(
    val ingredients: List<String>
)
