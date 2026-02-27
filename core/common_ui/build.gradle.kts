plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.natmusic.core.common_ui"
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
    // :core:mvi interfaces (ViewSingleEvent) needed by collectSingleEvent
    implementation(project(":core:mvi"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    // Google Fonts provider — downloads Poppins + Inter at runtime
    // Certs are declared in res/values/font_certs.xml
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    // lifecycle-runtime-compose: LocalLifecycleOwner + collectAsStateWithLifecycle
    // + repeatOnLifecycle — required by collectSingleEvent extension
    implementation(libs.androidx.lifecycle.runtime.compose)
    // Coil – required by MusicItemCard, LibraryItem, SongThumbnail
    implementation(libs.coil.compose)

    debugImplementation(libs.androidx.ui.tooling)
}

