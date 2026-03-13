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
include(":core:mockdata")     // Centralized Mock Data for development

// ─── Feature modules ──────────────────────────────────────────────────────────
include(":feature:home")      // Always-present bottom-nav container (Home · Explore · Library)
include(":feature:login")     // DFM candidate — Koin modules loaded on demand
include(":feature:setting")   // DFM candidate — Koin modules loaded on demand
