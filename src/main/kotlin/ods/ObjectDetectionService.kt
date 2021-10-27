package ods

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.kotlinx.dl.api.inference.loaders.ONNXModelHub
import org.jetbrains.kotlinx.dl.api.inference.objectdetection.DetectedObject
import org.jetbrains.kotlinx.dl.api.inference.onnx.ONNXModels
import org.jetbrains.kotlinx.dl.api.inference.onnx.objectdetection.SSDObjectDetectionModel
import java.io.File
import kotlin.io.path.writeBytes

class ObjectDetectionService(private val maxResults: Int = 10, private val minProbability: Double = 0.25) {

    private val model: SSDObjectDetectionModel by lazy {
        val modelHub = ONNXModelHub(cacheDirectory = File("cache/pretrainedModels"))
        ONNXModels.ObjectDetection.SSD.pretrainedModel(modelHub)
    }

    fun detectObjects(file: File): List<DetectedObject> {
        if (!file.exists()) throw IllegalArgumentException("File not found ${file.absoluteFile}")
        return model.detectObjects(imageFile = file, topK = maxResults).filter { it.probability >= minProbability }
    }

    // The blocking IO code is offloaded to the IO Dispatcher, so we can safely ignore warnings about
    // calling blocking code in a non-blocking context...
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun detectObjects(bytes: ByteArray): List<DetectedObject> = withContext(Dispatchers.IO) {
        val tmpFile = kotlin.io.path.createTempFile()
        tmpFile.writeBytes(bytes)
        detectObjects(tmpFile.toFile())
    }
}
