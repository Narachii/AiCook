package org.example.project.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.entity.Recipe
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RecipeContent(
    modifier: Modifier = Modifier,
    recipe: Recipe,
    onClick: () -> Unit = {},
) {

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(
            text = recipe.title,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = recipe.description,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        )
        Row(Modifier.padding(top = 8.dp)) {
            Text(
                text = "CookingTime: ",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            )
            Text(
                text = recipe.cookTime,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            )
        }
        Row(Modifier.padding(top = 8.dp)) {
            Text(
                text = "Servings: ",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            )
            Text(
                text = recipe.servings,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            )
        }
        Column(Modifier.padding(top = 8.dp)) {
            Text(
                text = "Ingredients",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
            )
            recipe.ingredients.forEach {
                Row(
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = " - ",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = it,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    )
                }
            }
            Column(Modifier.padding(top = 8.dp)) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = "Steps",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                )
                recipe.steps.forEachIndexed { index, step ->
                    Row(
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 4.dp),
                            text = "${index + 1}.",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        )
                        Text(
                            text = step,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Button(
                onClick = { onClick.invoke() }) {
                Text("Go Home")
            }
        }
    }
}


@Preview()
@Composable
fun PreviewRecipeContent() {
    RecipeContent(
        recipe = Recipe(
            title = "Tomato-Roasted Onions With So Much Paprika",
            description = "A quick and easy side dish using just potatoes and tomatoes. Perfect for a simple lunch or dinner!",
            cookTime = "10 min",
            ingredients = listOf("Tomato", "Onion", "Paprika"),
            servings = "2",
            steps = listOf(
                "Wash and dice the potatoes into small, bite-sized pieces.  Peeling is optional.",
                "Wash and chop the tomato into similar-sized pieces.",
                "Heat the olive oil in a pan over medium heat.",
                "Add the diced potatoes to the pan and cook for about 15 minutes, stirring occasionally, until they start to soften and brown slightly.",
                "Add the chopped tomato, salt, pepper, and paprika (if using) to the pan.",
                "Continue to cook for another 5-10 minutes, stirring occasionally, until the tomatoes have softened and the potatoes are cooked through and slightly crispy.",
                "Serve hot as a side dish.",
            ),
        )
    )
}