package org.example.project

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.project.entity.Result
import org.example.project.networks.ApiResult
import org.example.project.networks.GeminiNetworkClient
import org.example.project.entity.Recipe

class RecipeViewModel(private val client: GeminiNetworkClient) : ViewModel() {
    private val _viewState = MutableStateFlow<Result<Recipe>>(Result.Loading)
    val viewState: StateFlow<Result<Recipe>>
        get() = _viewState

    suspend fun askRecipe(ingredients: List<String>) {
        when (val result = client.askRecipe(ingredients)) {
            is ApiResult.Success -> _viewState.value = Result.Success(result.data)
            is ApiResult.Error -> _viewState.value = Result.Error(result.message)
        }
    }
}