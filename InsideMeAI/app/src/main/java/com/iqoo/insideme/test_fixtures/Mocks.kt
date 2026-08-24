package com.iqoo.insideme.test_fixtures

import com.iqoo.insideme.hardware.DeviceCapabilities
import com.iqoo.insideme.hardware.LocalSpeechToText
import com.iqoo.insideme.hardware.OfficeKitBridge
import java.io.File

class MockDeviceCapabilities : DeviceCapabilities {
    override fun hasCamera() = true
    override fun hasMicrophone() = true
    override fun supportsLocalAI() = true
    override fun supportsNpu() = true
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
        return "Compare this with the last time I saw it."
    }
}
