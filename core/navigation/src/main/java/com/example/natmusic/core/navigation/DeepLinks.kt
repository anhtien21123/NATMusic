package com.example.natmusic.core.navigation

import android.content.Intent
import android.net.Uri

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 *  DEEP LINK CONSTANTS & PARSER
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 *  URL scheme:  natmusic://
 *  Authority:   natmusic.example.com  (also matches bare host "detail" for brevity)
 *
 *  Supported deep links:
 *  ┌──────────────────────────────────────────────┬──────────────────────────┐
 *  │  URI                                         │  Resolved AppRoute       │
 *  ├──────────────────────────────────────────────┼──────────────────────────┤
 *  │  natmusic://natmusic.example.com/detail/123  │  AppRoute.Detail("123")  │
 *  │  natmusic://natmusic.example.com/detail/123  │  AppRoute.Detail("123",  │
 *  │    ?origin=explore                           │    origin="explore")     │
 *  │  natmusic://natmusic.example.com/setting     │  AppRoute.Setting        │
 *  │  (everything else)                           │  null  → caller decides  │
 *  └──────────────────────────────────────────────┴──────────────────────────┘
 *
 *  AndroidManifest intent filter (add to :app's MainActivity):
 *  ```xml
 *  <intent-filter android:autoVerify="true">
 *      <action android:name="android.intent.action.VIEW"/>
 *      <category android:name="android.intent.category.DEFAULT"/>
 *      <category android:name="android.intent.category.BROWSABLE"/>
 *      <data android:scheme="natmusic"
 *            android:host="natmusic.example.com"/>
 *  </intent-filter>
 *  ```
 */
object DeepLinks {

    const val SCHEME    = "natmusic"
    const val HOST      = "natmusic.example.com"
    const val BASE_URL  = "$SCHEME://$HOST"

    // ── Path segments ─────────────────────────────────────────────────────────
    const val PATH_DETAIL  = "detail"
    const val PATH_SETTING = "setting"

    // ── Query keys ────────────────────────────────────────────────────────────
    const val QUERY_ORIGIN = "origin"

    // ── Pre-built URI templates (useful for testing / sharing) ────────────────
    fun detailUri(id: String, origin: String = "deeplink"): Uri =
        Uri.parse("$BASE_URL/$PATH_DETAIL/$id?$QUERY_ORIGIN=$origin")

    fun settingUri(): Uri =
        Uri.parse("$BASE_URL/$PATH_SETTING")

    // ── Resolver ──────────────────────────────────────────────────────────────

    /**
     * Converts an incoming [Intent] to an [AppRoute].
     * Returns `null` when the intent has no deep link or the URI is unrecognized —
     * the caller should fall back to the default start destination ([AppRoute.Login]).
     *
     * Usage in AppNavHost:
     * ```kotlin
     * val activity  = LocalContext.current as Activity
     * val startRoute = remember { DeepLinks.resolve(activity.intent) ?: AppRoute.Login }
     * ```
     */
    fun resolve(intent: Intent?): AppRoute? {
        val uri = intent?.data ?: return null
        if (uri.scheme != SCHEME) return null

        return when (uri.pathSegments.firstOrNull()) {
            PATH_DETAIL -> {
                val id     = uri.pathSegments.getOrNull(1) ?: return null
                val origin = uri.getQueryParameter(QUERY_ORIGIN) ?: "deeplink"
                AppRoute.Detail(id = id, origin = origin)
            }
            PATH_SETTING -> AppRoute.Setting
            else -> null
        }
    }

    /**
     * Resolves a deep-link intent into the FULL back-stack that should be
     * pre-populated so the user can press Back naturally.
     *
     * Examples:
     *  • natmusic://…/detail/123  → [Login, Main, Detail("123")]  ← if unauthenticated
     *  • natmusic://…/setting     → [Login, Main, Setting]
     *
     * When the user is already authenticated the caller should swap Login for Main
     * as the first entry.
     */
    fun resolveBackStack(intent: Intent?, authenticated: Boolean = false): List<AppRoute> {
        val deepRoute = resolve(intent) ?: return listOf(AppRoute.Login)

        val root: AppRoute = if (authenticated) AppRoute.Main else AppRoute.Login
        return when (deepRoute) {
            AppRoute.Login   -> listOf(AppRoute.Login)
            AppRoute.Main    -> listOf(root)
            AppRoute.Setting -> listOf(root, AppRoute.Main, AppRoute.Setting)
                .distinct()
            is AppRoute.Detail -> listOf(root, AppRoute.Main, deepRoute)
                .distinct()
        }
    }
}

