package org.example.project.detector

// Credit: https://github.com/surendramaran/YOLOv8-TfLite-Object-Detector/tree/main
data class BoundingBox(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
    val cx: Float,
    val cy: Float,
    val w: Float,
    val h: Float,
    val cnf: Float,
    val cls: Int,
    val clsName: String
)
