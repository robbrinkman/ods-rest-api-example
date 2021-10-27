package ods

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json
import java.util.*


fun main(args: Array<String>): Unit = EngineMain.main(args)

private val objectDetectionService = ObjectDetectionService()

fun Application.module() {
    install(ContentNegotiation) {
        json(Json)
    }
    routing {
        get("/") {
            call.respondText("ODS")
        }
        post("/") {
            val odsRequest = call.receive<ODSRequest>()

            val detectedObjects = objectDetectionService
                .detectObjects(Base64.getDecoder().decode(odsRequest.image))
                .map { DetectedObject(it.classLabel, it.probability, BoundingBox(it.xMax, it.xMin, it.yMax, it.yMin)) }

            call.respond(ODSResponse(odsRequest.id, detectedObjects))
        }
    }
}

