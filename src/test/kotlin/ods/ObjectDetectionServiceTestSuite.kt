package ods

import org.jetbrains.kotlinx.dl.api.inference.objectdetection.DetectedObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.io.File
import kotlin.test.assertContentEquals

internal class ObjectDetectionServiceTestSuite {

    private val objectDetectionService = ObjectDetectionService(10, 0.25)

    @Test
    fun predictionSSDTest() {
        assertAll(
            // Picture by Anna Rye via Pexels
            { verifyDetection("snapshots/pexels-anna-rye-9975158.jpg", listOf("person")) },
            // Picture by Rachel Claire via Pexels
            { verifyDetection("snapshots/pexels-rachel-claire-6761052.jpg", listOf("person", "car", "car", "truck", "car")) },
        )
    }

    private fun verifyDetection(fileName: String, detectedObjectClasses: List<String>) {
        val result = objectDetectionService.detectObjects(getFileFromResource(fileName))
        assertContentEquals(detectedObjectClasses, result.map(DetectedObject::classLabel))
    }

     private fun getFileFromResource(fileName: String): File {
        val resource = javaClass.classLoader.getResource(fileName)
            ?: throw IllegalArgumentException("File not found! $fileName")
        return File(resource.toURI())
    }
}
