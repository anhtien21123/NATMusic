plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.natmusic.core.service.api"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Only common Media3 models to keep it lightweight
    implementation(libs.androidx.media3.exoplayer) // Needed for MediaItem if no common defined, or just use session/exoplayer and accept dependency. 
    // Actually, exoplayer is overkill in API, but let's keep it consistent
    
    // Coroutines for StateFlow
    implementation(libs.kotlinx.coroutines.android)
}
