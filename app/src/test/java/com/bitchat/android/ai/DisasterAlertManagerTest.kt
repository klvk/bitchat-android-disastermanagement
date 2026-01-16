package com.bitchat.android.ai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DisasterAlertManagerTest {

    @Test
    fun fetchActiveAlerts_returnsMockAlerts() {
        val alerts = DisasterAlertManager.fetchActiveAlerts()
        assertTrue(alerts.isNotEmpty())
        assertEquals("Flood", alerts[0].alert_type)
        assertEquals("Critical", alerts[0].severity)
    }

    @Test
    fun generateBroadcastMessage_formatsCorrectly_forFlood() {
        val alert = DisasterAlertManager.ServerAlert(
            alert_type = "Flood",
            severity = "Critical",
            title = "Flood Warning",
            message = "Rising water",
            district = "Hyderabad",
            mandal = "Charminar",
            confidence = 90.0,
            affected_population = 1000,
            expires_at = null
        )

        val message = DisasterAlertManager.generateBroadcastMessage(alert)
        val expected = "🚨 CRITICAL ALERT: Flood Warning in Hyderabad. Rising water Precautions: Seek high ground immediately. Do not walk or drive through flood waters."
        assertEquals(expected, message)
    }

    @Test
    fun generateBroadcastMessage_formatsCorrectly_forFire() {
        val alert = DisasterAlertManager.ServerAlert(
            alert_type = "Fire",
            severity = "High",
            title = "Forest Fire",
            message = "Smoke detected",
            district = "Adilabad",
            mandal = null,
            confidence = 80.0,
            affected_population = 500,
            expires_at = null
        )

        val message = DisasterAlertManager.generateBroadcastMessage(alert)
        val expected = "🚨 HIGH ALERT: Forest Fire in Adilabad. Smoke detected Precautions: Evacuate immediately. Stay low to avoid smoke."
        assertEquals(expected, message)
    }
}
