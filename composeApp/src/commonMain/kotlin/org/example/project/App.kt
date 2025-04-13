package org.example.project

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import io.ktor.client.engine.HttpClientEngine
import org.example.project.navigation.createApplicationNavGraph
import org.example.project.networks.GeminiNetworkClient
import org.example.project.networks.createHttpClient

@Composable
@Preview
fun App(engine: HttpClientEngine) {
    val client = GeminiNetworkClient(createHttpClient(engine))
    val viewModel = RecipeViewModel(client)
    val navController = rememberNavController()

    MaterialTheme {
        createApplicationNavGraph(navController, viewModel)
    }
}