package org.example.project.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    title: String,
) {
    Column(
        modifier.padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            fontSize = 32.sp,
        )
        CircularProgressIndicator(
            modifier = Modifier.width(128.dp).weight(2f),
            color = MaterialTheme.colorScheme.secondary,
        )
    }

}