package com.bitchat.android.ai

import java.util.Date

/**
 * Manages integration with the CAPSTONE project disaster alert server.
 * Handles fetching active alerts and formatting them for broadcast.
 */
object DisasterAlertManager {

    /**
     * Matches the JSON structure from the backend /api/alerts endpoint
     */
    data class ServerAlert(
        val alert_type: String,
        val severity: String,
        val title: String,
        val message: String,
        val district: String,
        val mandal: String?,
        val confidence: Double?,
        val affected_population: Int?,
        val expires_at: String?
    )

    /**
     * Simulates fetching active alerts from the server.
     * In a production environment, this would use Retrofit/OkHttp to call
     * GET http://<server-ip>:8000/api/alerts
     */
    fun fetchActiveAlerts(): List<ServerAlert> {
        // Mock response simulating a flood in Hyderabad
        // In a real scenario, network request goes here.
        return listOf(
            ServerAlert(
                alert_type = "Flood",
                severity = "Critical",
                title = "Flash Flood Warning",
                message = "Heavy rains have caused severe waterlogging in low-lying areas.",
                district = "Hyderabad",
                mandal = "Charminar",
                confidence = 95.0,
                affected_population = 50000,
                expires_at = "2024-12-31T23:59:59Z"
            )
        )
    }

    /**
     * Generates the structured broadcast message.
     * Format: "🚨 [SEVERITY] ALERT: [TITLE] in [DISTRICT]. [MESSAGE]. Precautions: [PRECAUTIONS]"
     */
    fun generateBroadcastMessage(alert: ServerAlert): String {
        val precautions = getPrecautionsForType(alert.alert_type)
        return "🚨 ${alert.severity.uppercase()} ALERT: ${alert.title} in ${alert.district}. ${alert.message} Precautions: $precautions"
    }

    private fun getPrecautionsForType(type: String): String {
        return when (type.lowercase()) {
            "flood" -> "Seek high ground immediately. Do not walk or drive through flood waters."
            "fire" -> "Evacuate immediately. Stay low to avoid smoke."
            "earthquake" -> "Drop, Cover, and Hold On. Stay away from windows."
            "storm" -> "Stay indoors. Stay away from windows and doors."
            else -> "Follow local emergency instructions."
        }
    }
}
