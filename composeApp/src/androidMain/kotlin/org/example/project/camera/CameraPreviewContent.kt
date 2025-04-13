package org.example.project.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.example.project.navigation.IngredientRoute


@Composable
fun CameraPreviewContent(
    modifier: Modifier = Modifier,
    appContext: Context,
    controller: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    navHostController: NavHostController,
) {
    val viewModel = viewModel<CameraPreviewViewModel>()
    val takenImage by viewModel.takenImage.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        if (takenImage == null) {
            AndroidView(
                factory = {
                    PreviewView(it).apply {
                        this.controller = controller
                        controller.bindToLifecycle(lifecycleOwner)
                        controller.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(52.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                IconButton(
                    onClick = {
                        viewModel.takePhoto(
                            appContext,
                            controller,
                        )
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(360.dp),
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Take Photo",
                    )
                }

            }
        } else {
            Image(
                bitmap = takenImage!!.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier.fillMaxSize(),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(46.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(
                    onClick = {
                        viewModel::resetImage.invoke()
                        viewModel.detector?.restart()
                    },
                ) {
                    Text("Back to Camera")
                }
                TextButton(
                    onClick = {
                        navHostController.navigate(IngredientRoute(ingredients = viewModel.detectedObjects))
                    }
                ) {
                    Text("Next")
                }
            }
        }
    }
}