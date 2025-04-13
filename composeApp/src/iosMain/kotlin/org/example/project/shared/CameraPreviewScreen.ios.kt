package org.example.project.shared

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
actual fun CameraPreviewScreen(modifier: Modifier, navHostController: NavHostController) {
    Text("This is from iOS!")
}