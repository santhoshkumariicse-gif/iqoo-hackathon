package com.iqoo.insideme.repositories

import android.util.Log
import com.iqoo.insideme.ai.GroundedResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class OfficeKitSyncRepository {

    suspend fun syncToEvidenceDesk(
        entityId: String,
        response: GroundedResponse,
        serverIp: String = com.iqoo.insideme.device.runtime.AppConfig.officeKitServerIp
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("http://\$serverIp:8080/api/evidence/sync")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")

            val jsonPayload = """
                {
                    "entityId": "$entityId",
                    "answer": "${response.answer.replace("\"", "\\\"")}",
                    "confidence": "${response.confidence}",
                    "evidenceChain": [
                        ${response.evidenceIds.joinToString(",") { "\"$it\"" }}
                    ],
                    "changes": [
                        ${response.changes.joinToString(",") { change ->
                            """
                            {
                                "attribute": "${change.category}",
                                "previous": "UNKNOWN",
                                "current": "${change.description.replace("\"", "\\\"")}",
                                "confidence": ${change.confidence}
                            }
                            """.trimIndent()
                        }}
                    ]
                }
            """.trimIndent()

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(jsonPayload)
            }

            val responseCode = connection.responseCode
            Log.d("OfficeKitSync", "Sync Response Code: \$responseCode")
            
            responseCode == 200
        } catch (e: Exception) {
            Log.e("OfficeKitSync", "Sync failed", e)
            false
        }
    }
}
