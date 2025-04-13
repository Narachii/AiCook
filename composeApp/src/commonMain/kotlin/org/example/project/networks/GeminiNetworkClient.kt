package org.example.project.networks

import com.diamondedge.logging.logging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.example.project.entity.Recipe
import org.example.project.shared.AppSecretConfig

class GeminiNetworkClient(
    private val httpClient: HttpClient
) {
    suspend fun askRecipe(ingredients: List<String>): ApiResult<Recipe> {
        val requestBody = RecipeRequest(ingredients)
        // Generate AppSecretConfig by executing ./gradlew clean generateBuildKonfig
        val response = httpClient.post(AppSecretConfig.GEMINI_FUNCTION_URL) {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(RecipeRequest.serializer(), requestBody))
        }

        val log = logging("API Response")
        return when(response.status.value) {
            in 200 .. 299 -> {
                log.info { response.bodyAsText() }
                val recipeResponse = response.body<RecipeResponse>()
                ApiResult.Success(recipeResponse.recipe)
            }
            else  -> {
                log.info { response.bodyAsText() }
                log.info { response.status }

                ApiResult.Error("Error: ${response.status.value}")
            }
        }
    }
}