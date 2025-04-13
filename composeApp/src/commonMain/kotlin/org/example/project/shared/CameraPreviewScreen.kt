package org.example.project.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
expect fun CameraPreviewScreen(modifier: Modifier, navHostController: NavHostController)

//interface TestCompose {
//    @Composable
//    fun Foo() {
//
//    }
//
//    @Composable
//    fun Bar() {
//
//    }
//}