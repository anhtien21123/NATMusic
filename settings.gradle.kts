pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NATMusic"

// ─── App shell ────────────────────────────────────────────────────────────────
include(":app")

// ─── Core modules ─────────────────────────────────────────────────────────────
include(":core:mvi")          // BaseViewModel · ViewState · ViewIntent · ViewSingleEvent
include(":core:common_ui")    // Design System · Theme · Components · collectSingleEvent
include(":core:navigation")   // ALL @Serializable routes · deep-link constants · NavBackStack extensions
include(":core:service-api")  // Interfaces & Media models (Shared)
include(":core:service-impl") // Media3 implementation (internal)
// ─── Feature modules ──────────────────────────────────────────────────────────
include(":feature:home")
include(":feature:home:domain")
include(":feature:home:data")
include(":feature:login")
include(":feature:login:domain")
include(":feature:login:data")
include(":feature:setting")
include(":feature:setting:domain")
include(":feature:setting:data")
