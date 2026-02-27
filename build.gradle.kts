// Top-level build file where you can add configuration options common to all sub-projects/modules.
//
// Rule: every plugin used by ANY submodule MUST be declared here with `apply false`.
// Without this, Gradle sees the plugin "already on the classpath with an unknown version"
// when a module tries to apply it — causing the InvalidPluginRequestException.
plugins {
    // :app
    alias(libs.plugins.android.application) apply false

    // :core:* and :feature:* library modules
    alias(libs.plugins.android.library) apply false

    // All Kotlin modules
    alias(libs.plugins.kotlin.android) apply false

    // Modules with @Composable functions
    alias(libs.plugins.kotlin.compose) apply false

    // Modules with @Serializable routes (:core:navigation, :feature:home, :app)
    alias(libs.plugins.kotlin.serialization) apply false
}