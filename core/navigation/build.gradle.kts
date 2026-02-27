/**
 * :core:navigation — pure route-definition library.
 *
 * Design principle:
 *  • ZERO Compose / Nav3 runtime dependency.
 *  • ONLY kotlinx-serialization-json for @Serializable route objects.
 *
 * Why keep it separate from :core:mvi and :core:common_ui?
 *  • Feature modules that only navigate (emit a SingleEvent with a route)
 *    need this module but NOT the full Compose stack.
 *  • Keeps the route definitions as a lightweight, fast-compiling artifact.
 *
 * Dependency graph (acyclic):
 *  :app  ──→  :feature:*  ──→  :core:navigation
 *                          ──→  :core:mvi
 *                          ──→  :core:common_ui
 */
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.natmusic.core.navigation"
    compileSdk = 36

    defaultConfig { minSdk = 24 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    // kotlinx-serialization-json is the ONLY runtime dep:
    //   → encodes/decodes @Serializable route objects for deep links.
    //   → Nav3 runtime reads these objects directly; no string serialisation needed
    //     for in-process navigation (only for deep links / saved state).
    api(libs.kotlinx.serialization.json)
}

