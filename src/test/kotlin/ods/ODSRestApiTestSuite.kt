package ods

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class ODSRestApiTestSuite {

    @Test
    fun testGet() {
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("ODS", response.content)
            }
        }
    }



//    @Test
//    fun testInvalidPost() {
//        withTestApplication({ module() }) {
//            handleRequest(HttpMethod.Post, "/") {
//                addHeader("Content-Type", "application/json")
//                setBody("{}")
//            }.apply {
//                assertEquals(HttpStatusCode.OK, response.status())
//                assertNotNull(response.content)
//
//                val response = Json.decodeFromString<IRSResponse>(response.content!!)
//
//                assertNotNull(response.id)
//                assertEquals(1, response.detectedObjects.size)
//                assertEquals("person", response.detectedObjects[0].labelName)
//            }
//        }
//    }



    @ExperimentalSerializationApi
    @Test
    fun testPost() {
        withTestApplication({ module() }) {
            val id = generateId()
            handleRequest(HttpMethod.Post, "/") {
                addHeader("Content-Type", "application/json")
                setBody(Json.encodeToString(ODSRequest(id, encodeImage("snapshots/pexels-anna-rye-9975158.jpg"))))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertNotNull(response.content)

                val response = Json.decodeFromString<ODSResponse>(response.content!!)
                assertEquals(id, response.id)
                assertEquals(1, response.detectedObjects.size)
                assertEquals("person", response.detectedObjects[0].labelName)
            }
        }
    }

    private fun generateId() : String = UUID.randomUUID().toString()

    private fun encodeImage(fileName : String) =  Base64.getEncoder().encodeToString(getFileFromResource(fileName).readBytes())

    private fun getFileFromResource(fileName: String): File {
        val resource = javaClass.classLoader.getResource(fileName) ?: throw IllegalArgumentException("File not found! $fileName")
        return File(resource.toURI())
    }
}
