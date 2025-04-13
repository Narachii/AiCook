package org.example.project.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun IngredientContent(
    modifier: Modifier = Modifier,
    ingredients: List<String>,
    onClick: (List<String>) -> Unit,
) {

    var ingredientList by remember {
        mutableStateOf(
            ingredients.toMutableList()
        )
    }

    Column(
        modifier.padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Found Ingredients",
            fontSize = 32.sp,
        )
        Column(
            modifier = Modifier.weight(3f),
        ) {
            ingredientList.forEachIndexed { index, item ->
                TextField(
                    value = item,
                    onValueChange = { newText ->
                        val updatedList = ingredientList.toMutableList()
                        updatedList[index] = newText
                        ingredientList = updatedList
                    },
                )
            }
            Button(
                onClick = {
                    val updatedList = ingredientList.toMutableList()
                    updatedList.add("")
                    ingredientList = updatedList
                }
            ) {
                Text("+ Add")
            }
        }
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Button(
                onClick = {
                    ingredientList.removeAll(listOf(""))
                    onClick(ingredientList)
                }
            ) {
                Text("Find Recipe")
            }
        }
    }
}

@Composable
@Preview
fun PreviewIngredientScreen() {
    IngredientContent(
        modifier = Modifier.fillMaxSize(),
        ingredients = mutableListOf("Tomato", "Onion", "Paprika"),
        onClick = {},
    )
}
