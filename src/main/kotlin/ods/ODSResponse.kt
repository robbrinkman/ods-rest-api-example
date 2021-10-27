package ods

import kotlinx.serialization.Serializable

@Serializable
data class ODSResponse(val id: String, val detectedObjects: List<DetectedObject> = emptyList())

@Serializable
data class DetectedObject(
    val labelName: String,
    val probability: Float,
    val boundingBox: BoundingBox
)

@Serializable
data class BoundingBox(
    val xMax: Float,
    val xMin: Float,
    val yMax: Float,
    val yMin: Float
)