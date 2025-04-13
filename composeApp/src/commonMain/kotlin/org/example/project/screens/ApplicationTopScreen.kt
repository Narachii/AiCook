package org.example.project.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.example.project.navigation.CameraPreviewRoute
import org.example.project.screens.components.ApplicationTopContent

@Composable
fun ApplicationTopScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    ApplicationTopContent(modifier) { navController.navigate(CameraPreviewRoute) }
}
