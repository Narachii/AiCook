package org.example.project.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.detector.Constants
import org.example.project.detector.Detector

class CameraPreviewViewModel : ViewModel() {
    var detector: Detector? = null

    private val _takenImage = MutableStateFlow<Bitmap?>(null)
    val takenImage: StateFlow<Bitmap?> = _takenImage.asStateFlow()
    val detectedObjects: MutableList<String> = mutableListOf()

    fun resetImage() {
        detectedObjects.clear()
        _takenImage.update { null }
    }

    fun takePhoto(
        appContext: Context,
        controller: LifecycleCameraController,
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(appContext),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exception: ImageCaptureException) {
                    Log.e("ImageCapture", "Photo capture failed: ${exception.message}", exception)
                }

                @OptIn(ExperimentalGetImage::class)
                override fun onCaptureSuccess(image: ImageProxy) {
                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )

                    runYoloObjectDetection(appContext, bitmap = rotatedBitmap)
                    image.close()
                }
            }
        )
    }

    /**
     * YOLO object detection
     *      A function to run object detection
     */
    fun runYoloObjectDetection(context: Context, bitmap: Bitmap) {

        if (detector == null) {
            detector = Detector(
                context = context,
                modelPath = Constants.MODEL_PATH,
                labelPath = Constants.LABELS_PATH,
            )
        }

        val resultBitmap = detector?.detect(bitmap)
        detector?.resultLabels?.let {
            detectedObjects.addAll(it)
        }

        _takenImage.update { resultBitmap }
    }

    //private fun runObjectDetection(
    //    bitmap: Bitmap,
    //    appContext: Context,
    //) {
    //    val image = TensorImage.fromBitmap(bitmap)

    //    val options = ObjectDetector.ObjectDetectorOptions.builder()
    //        .setMaxResults(5)
    //        .setScoreThreshold(0.5f)
    //        .build()
    //    val detector = ObjectDetector.createFromFileAndOptions(
    //        appContext, // the application context
    //        "model.tflite", // must be same as the filename in assets folder
    //        // detect.tflite java.lang.IllegalArgumentException: Error occurred when initializing ObjectDetector: Object detection models require TFLite Model Metadata but none was found
    //        options
    //    )

    //    val results = detector.detect(image)
    //    val resultImage = drawBox(results, bitmap)
    //    debugPrint(results)
    //    for (result in results) {
    //        val label = result.categories[0].label
    //        detectedObjects.add(label)
    //    }
    //    _takenImage.update { resultImage }
    //}

    //private fun debugPrint(results : List<Detection>) {
    //    for ((i, obj) in results.withIndex()) {
    //        val box = obj.boundingBox

    //        Log.d(TAG, "Detected object: ${i} ")
    //        Log.d(TAG, "  boundingBox: (${box.left}, ${box.top}) - (${box.right},${box.bottom})")

    //        for ((j, category) in obj.categories.withIndex()) {
    //            Log.d(TAG, "    Label $j: ${category.label}")
    //            val confidence: Int = category.score.times(100).toInt()
    //            Log.d(TAG, "    Confidence: ${confidence}%")
    //        }
    //    }
    //}

    //private fun drawBox(results: List<Detection>, bitmap: Bitmap) : Bitmap {
    //    val resultToDisplay = results.map {
    //        // Get the top-1 category and craft the display text
    //        val category = it.categories.first()
    //        val text = "${category.label}, ${category.score.times(100).toInt()}%"

    //        // Create a data object to display the detection result
    //        DetectionResult(it.boundingBox, text)
    //    }
    //    // Draw the detection result on the bitmap and show it.
    //    val imgWithResult = drawDetectionResult(bitmap, resultToDisplay)

    //    return imgWithResult
    //}

    ///**
    // * drawDetectionResult(bitmap: Bitmap, detectionResults: List<DetectionResult>
    // *      Draw a box around each objects and show the object's name.
    // */
    //private fun drawDetectionResult(
    //    bitmap: Bitmap,
    //    detectionResults: List<DetectionResult>
    //): Bitmap {
    //    val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    //    val canvas = Canvas(outputBitmap)
    //    val pen = Paint()

    //    pen.textAlign = Paint.Align.LEFT

    //    detectionResults.forEach {
    //        // draw bounding box
    //        pen.color = Color.RED
    //        pen.strokeWidth = 8F
    //        pen.style = Paint.Style.STROKE
    //        val box = it.boundingBox
    //        canvas.drawRect(box, pen)


    //        val tagSize = Rect(0, 0, 0, 0)

    //        // calculate the right font size
    //        pen.style = Paint.Style.FILL_AND_STROKE
    //        pen.color = Color.YELLOW
    //        pen.strokeWidth = 2F

    //        pen.textSize = 96F
    //        pen.getTextBounds(it.text, 0, it.text.length, tagSize)
    //        val fontSize: Float = pen.textSize * box.width() / tagSize.width()

    //        // adjust the font size so texts are inside the bounding box
    //        if (fontSize < pen.textSize) pen.textSize = fontSize

    //        var margin = (box.width() - tagSize.width()) / 2.0F
    //        if (margin < 0F) margin = 0F
    //        canvas.drawText(
    //            it.text, box.left + margin,
    //            box.top + tagSize.height().times(1F), pen
    //        )
    //    }
    //    return outputBitmap
    //}

    ///**
    // * DetectionResult
    // *      A class to store the visualization info of a detected object.
    // */
    //data class DetectionResult(val boundingBox: RectF, val text: String)
}