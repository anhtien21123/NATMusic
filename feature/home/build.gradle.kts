plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.natmusic.feature.home"
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core modules
    implementation(project(":core:mvi"))
    implementation(project(":core:common_ui"))
    // :core:navigation exposes AppRoute / MainRoute / NavBackStack extensions
    implementation(project(":core:navigation"))
    implementation(project(":core:mockdata"))
    implementation(project(":core:service-api"))
    
    // Media3 (Required for MediaItem in ViewModel)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.session)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.ui.tooling.preview)
    // lifecycle-aware state collection + collectAsStateWithLifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.coil.compose)

    debugImplementation(libs.androidx.ui.tooling)
}

