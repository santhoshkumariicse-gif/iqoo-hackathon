plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.iqoo.insideme"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.iqoo.insideme"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { 
        compose = true 
        buildConfig = true
    }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.10" }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    
    // Room Database
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:\$room_version")
    implementation("androidx.room:room-ktx:\$room_version")
    ksp("androidx.room:room-compiler:\$room_version")
    
    // CameraX
    val camerax_version = "1.3.0"
    implementation("androidx.camera:camera-core:\$camerax_version")
    implementation("androidx.camera:camera-camera2:\$camerax_version")
    implementation("androidx.camera:camera-lifecycle:\$camerax_version")
    implementation("androidx.camera:camera-view:\$camerax_version")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Palette – real dominant colour extraction from captured images
    implementation("androidx.palette:palette-ktx:1.0.0")
}
