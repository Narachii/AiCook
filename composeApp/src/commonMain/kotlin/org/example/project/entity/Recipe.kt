package org.example.project.entity

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val title: String,
    val description: String,
    val cookTime: String,
    val servings: String,
    val ingredients: List<String>,
    val steps: List<String>,
)
