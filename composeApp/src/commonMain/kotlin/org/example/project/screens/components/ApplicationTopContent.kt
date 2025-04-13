package org.example.project.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ApplicationTopContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier.padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.weight(5f),
            text = "Ai Cooking",
            fontSize = 32.sp,
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Button(
                onClick = {
                    onClick()
                }
            ) {
                Text("Take Ingredients")
            }
        }
    }
}

@Composable
@Preview
fun PreviewApplicationTop() {
    ApplicationTopContent(
        onClick = {},
    )
}
