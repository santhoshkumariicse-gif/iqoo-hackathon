package com.iqoo.insideme.hardware

import java.io.File

interface DeviceCapabilities {
    fun hasCamera(): Boolean
    fun hasMicrophone(): Boolean
    fun supportsLocalAI(): Boolean
    fun supportsNpu(): Boolean
    fun supportsOfficeKit(): Boolean
}

interface OfficeKitBridge {
    suspend fun sendMemory(memoryId: String)
    suspend fun receiveFile(file: File)
    suspend fun sendReport(reportContent: String)
    suspend fun sendClipboardText(text: String)
    suspend fun openWorkspace(memoryId: String)
}

interface LocalSpeechToText {
    suspend fun transcribeAudio(audioByteArray: ByteArray): String
}

class VoiceQueryController(
    private val stt: LocalSpeechToText,
    private val capabilities: DeviceCapabilities
) {
    suspend fun processVoiceInput(audio: ByteArray): String {
        if (!capabilities.hasMicrophone()) throw Exception("No microphone available on this device.")
        
        val transcribedQuery = stt.transcribeAudio(audio)
        return transcribedQuery
    }
}


