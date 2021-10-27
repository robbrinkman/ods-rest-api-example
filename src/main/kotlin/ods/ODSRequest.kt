package ods

import kotlinx.serialization.Serializable

@Serializable
data class ODSRequest(val id: String, val image: String)