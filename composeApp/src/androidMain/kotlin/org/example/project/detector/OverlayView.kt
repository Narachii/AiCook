package org.example.project.detector

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ObjectDetectionOverlay(
    boundingBoxes: List<BoundingBox>,
    modifier: Modifier = Modifier,
    imageWidth: Int,
    imageHeight: Int
) {
    // Define paints using remember to avoid recreation on every recomposition
    val boxPaint = remember {
        Paint().apply {
            color = Color.Red // Use Compose Color
            strokeWidth = 8f  // Use Float for strokeWidth
            style = PaintingStyle.Stroke // Use Compose PaintingStyle
        }
    }
    val textBackgroundPaint = remember {
        Paint().apply {
            color = Color.Black
            style = PaintingStyle.Fill
        }
    }
    val textMeasurer = rememberTextMeasurer() // Use rememberTextMeasurer

    Box(
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            for (box in boundingBoxes) {
                // Calculate scaled coordinates based on image dimensions
                val left = box.x1 * imageWidth
                val top = box.y1 * imageHeight
                val right = box.x2 * imageWidth
                val bottom = box.y2 * imageHeight

                // Draw the bounding box
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(left, top),
                    size = Size(right - left, bottom - top),
                    style = Stroke(width = 8f)
                )

                // Draw the text background and text
                val drawableText = box.clsName

                // Measure text layout
                val textLayoutResult: TextLayoutResult = textMeasurer.measure(
                    text = drawableText,
                    style = TextStyle(color = Color.White, fontSize = 10.sp),
                    overflow = TextOverflow.Ellipsis // Handle long text if needed
                )

                val textWidth = textLayoutResult.size.width.toFloat()
                val textHeight = textLayoutResult.size.height.toFloat()
                val textPadding = 8f

                // Draw text background
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(left, top),
                    size = Size(textWidth + textPadding * 2, textHeight + textPadding * 2) // Adjust size for padding
                )

                // Draw the text using drawText
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(left + textPadding, top + textPadding)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ObjectDetectionOverlayPreview() {
    val previewBoxes = listOf(
        BoundingBox(
            x1 = 0.1f, y1 = 0.2f, x2 = 0.3f, y2 = 0.4f,
            cx = 0.2f, cy = 0.3f, w = 0.1f, h = 0.2f,
            cnf = 0.8f, cls = 0, clsName = "Person"
        ),
        BoundingBox(
            x1 = 0.5f, y1 = 0.6f, x2 = 0.8f, y2 = 0.9f,
            cx = 0.65f, cy = 0.75f, w = 0.3f, h = 0.3f,
            cnf = 0.9f, cls = 2, clsName = "Car"
        ),
        BoundingBox(
            x1 = 0.2f, y1 = 0.7f, x2 = 0.4f, y2 = 0.8f,
            cx = 0.3f, cy = 0.75f, w = 0.2f, h = 0.1f,
            cnf = 0.7f, cls = 1, clsName = "Dog"
        )
    )
    ObjectDetectionOverlay(
        boundingBoxes = previewBoxes,
        imageWidth = 640,
        imageHeight = 480,
        modifier = Modifier.fillMaxSize()
    )
}