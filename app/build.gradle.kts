plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.natmusic"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.natmusic"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { compose = true }
}

dependencies {
    // ─── Feature modules ──────────────────────────────────────────────────────
    // :app is the composition root — only :app depends on features
    implementation(project(":feature:home"))
    implementation(project(":feature:login"))
    implementation(project(":feature:setting"))

    // ─── Core modules ─────────────────────────────────────────────────────────
    implementation(project(":core:mvi"))
    implementation(project(":core:common_ui"))
    implementation(project(":core:navigation"))   // AppRoute, MainRoute, DeepLinks, NavBackStackExt
    implementation(project(":core:service"))      // Media3 Service · Player logic

    // ─── AndroidX shell ───────────────────────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    // ─── Jetpack Navigation 3 — sole navigation engine ────────────────────────
    // nav3-runtime: NavDisplay, rememberNavBackStack, NavBackStack, entryProvider
    implementation(libs.nav3.runtime)
    // lifecycle-viewmodel-navigation3: scopes ViewModelStore per NavEntry so
    // koinViewModel() creates ONE instance per screen (not shared across screens)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    // ─── Serialization (for @Serializable routes + deep-link restoration) ─────
    implementation(libs.kotlinx.serialization.json)

    // ─── Media3 ───────────────────────────────────────────────────────────────
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.session)

    // ─── Koin ─────────────────────────────────────────────────────────────────
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.androidx.navigation3.ui.android)

    // ─── Test ─────────────────────────────────────────────────────────────────
    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
