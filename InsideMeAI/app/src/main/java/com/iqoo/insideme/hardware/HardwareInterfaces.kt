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

// ==========================================
// MOCKS FOR DEVELOPMENT
// ==========================================

class MockDeviceCapabilities : DeviceCapabilities {
    override fun hasCamera() = true
    override fun hasMicrophone() = true
    override fun supportsLocalAI() = true
    override fun supportsNpu() = true // Assuming the iQOO hackathon device
    override fun supportsOfficeKit() = true
}

class MockOfficeKitBridge : OfficeKitBridge {
    override suspend fun sendMemory(memoryId: String) {
        println("OFFICE KIT: Sending Memory $memoryId to Laptop...")
    }

    override suspend fun receiveFile(file: File) {
        println("OFFICE KIT: Received ${file.name} from Laptop.")
    }

    override suspend fun sendReport(reportContent: String) {
        println("OFFICE KIT: Opening report workspace on Laptop...")
    }

    override suspend fun sendClipboardText(text: String) {
        println("OFFICE KIT: Text received from Laptop clipboard.")
    }

    override suspend fun openWorkspace(memoryId: String) {
        println("OFFICE KIT: Opening Memory $memoryId detailed view on Laptop.")
    }
}

class MockLocalSpeechToText : LocalSpeechToText {
    override suspend fun transcribeAudio(audioByteArray: ByteArray): String {
        return "Compare this with the last time I saw it." // Simulated user voice input
    }
}
