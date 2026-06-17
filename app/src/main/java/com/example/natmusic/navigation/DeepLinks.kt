package com.example.natmusic.navigation

import android.content.Intent
import android.net.Uri
import com.example.natmusic.core.navigation.Destination
import com.example.natmusic.feature.home.navigation.Detail
import com.example.natmusic.feature.home.navigation.Main
import com.example.natmusic.feature.login.navigation.Login
import com.example.natmusic.feature.setting.navigation.Setting

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 *  DEEP LINK CONSTANTS & PARSER (App Level)
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 *  URL scheme:  natmusic://
 *  Authority:   natmusic.example.com
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

    // ── Pre-built URI templates ────────────────────────────────────────────────
    fun detailUri(id: String, origin: String = "deeplink"): Uri =
        Uri.parse("$BASE_URL/$PATH_DETAIL/$id?$QUERY_ORIGIN=$origin")

    fun settingUri(): Uri =
        Uri.parse("$BASE_URL/$PATH_SETTING")

    // ── Resolver ──────────────────────────────────────────────────────────────

    /**
     * Converts an incoming [Intent] to a concrete feature [Destination].
     */
    fun resolve(intent: Intent?): Destination? {
        val uri = intent?.data ?: return null
        if (uri.scheme != SCHEME) return null

        return when (uri.pathSegments.firstOrNull()) {
            PATH_DETAIL -> {
                val id     = uri.pathSegments.getOrNull(1) ?: return null
                val origin = uri.getQueryParameter(QUERY_ORIGIN) ?: "deeplink"
                Detail(id = id, origin = origin)
            }
            PATH_SETTING -> Setting
            else -> null
        }
    }

    /**
     * Resolves a deep-link intent into the FULL back-stack.
     */
    fun resolveBackStack(intent: Intent?, authenticated: Boolean = false): List<Destination> {
        val deepRoute = resolve(intent) ?: return listOf(Login)

        val root = if (authenticated) Main else Login
        return when (deepRoute) {
            Login -> listOf(Login)
            Main -> listOf(root)
            Setting -> listOf(root, Main, Setting).distinct()
            is Detail -> listOf(root, Main, deepRoute).distinct()
            else -> listOf(root)
        }
    }
}
