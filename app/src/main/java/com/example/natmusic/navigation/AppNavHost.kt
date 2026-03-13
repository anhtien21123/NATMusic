package com.example.natmusic.navigation

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.natmusic.core.navigation.AppRoute
import com.example.natmusic.core.navigation.DeepLinks
import com.example.natmusic.core.navigation.popUpTo
import com.example.natmusic.feature.home.detail.DetailScreen
import com.example.natmusic.feature.home.presentation.HomeNavScreen
import com.example.natmusic.feature.login.LoginScreen
import com.example.natmusic.feature.setting.SettingScreen



/**
 * ═══════════════════════════════════════════════════════════════════════════════
 *  AppNavHost — Nav3 top-level navigation host
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 *  This is the SOLE navigation engine for the app.
 *  All screens are registered here; feature modules never see each other.
 *
 *  ── Dependency rule ──────────────────────────────────────────────────────────
 *
 *                    :core:navigation
 *                    (AppRoute, MainRoute)
 *                          ▲
 *          ┌───────────────┼───────────────┐
 *     :feature:home  :feature:login  :feature:setting
 *          └───────────────┼───────────────┘
 *                          ▲
 *                         :app
 *                   (AppNavHost lives here)
 *
 *  :feature:* modules depend on :core:navigation for route types.
 *  :app depends on all feature modules and owns the NavDisplay.
 *  No feature module depends on another feature module.
 *
 *  ── Back-stack mental model (Nav3-style, tự implement) ───────────────────────
 *
 *  Thay vì phụ thuộc trực tiếp vào Nav3 alpha, ta mô phỏng lại đúng mô hình:
 *
 *    • `backStack: MutableList<AppRoute>` là nguồn sự thật duy nhất.
 *    • Mỗi lần thêm/xoá phần tử, Compose recomposition.
 *    • Không NavController, không NavGraph XML.
 *
 *  ┌────────────────────────────────────────────────────────────────────┐
 *  │  Operation        │  Nav2 equivalent                               │
 *  ├────────────────────────────────────────────────────────────────────┤
 *  │  backStack.add(R) │  navController.navigate(R)                    │
 *  │  backStack        │  navController.navigate(R) {                  │
 *  │    .popUpTo<T>()  │    popUpTo<T> { inclusive = true }            │
 *  │    .add(R)        │  }                                            │
 *  │  backStack        │  navController.popBackStack()                 │
 *  │    .removeLastOrNull()                                            │
 *  └────────────────────────────────────────────────────────────────────┘
 *
 *  ── MVI + SingleEvent navigation contract ────────────────────────────────────
 *
 *  1. ViewModel emits  →  SingleEvent  (e.g. LoginContract.SingleEvent.NavigateToMain)
 *  2. Screen collects  →  via collectSingleEvent { ... }  (lifecycle-aware)
 *  3. Screen calls     →  onLoginSuccess()  (lambda from AppNavHost)
 *  4. AppNavHost does  →  backStack.popUpTo<AppRoute.Login>(inclusive = true)
 *                          backStack.add(AppRoute.Main)
 *
 *  Feature modules NEVER import AppRoute.  They only import route types from
 *  :core:navigation when their own SingleEvent needs to carry a route argument.
 *
 *  ── Deep link handling ───────────────────────────────────────────────────────
 *
 *  AndroidManifest.xml (add to MainActivity):
 *  ```xml
 *  <intent-filter android:autoVerify="true">
 *      <action android:name="android.intent.action.VIEW"/>
 *      <category android:name="android.intent.category.DEFAULT"/>
 *      <category android:name="android.intent.category.BROWSABLE"/>
 *      <data android:scheme="natmusic" android:host="natmusic.example.com"/>
 *  </intent-filter>
 *  ```
 *  Supported URIs → resolved back-stacks (see DeepLinks.kt):
 *   natmusic://natmusic.example.com/detail/abc123   → [Login, Main, Detail("abc123")]
 *   natmusic://natmusic.example.com/setting         → [Login, Main, Setting]
 */
@SuppressLint("ContextCastToActivity")
@Composable
fun AppNavHost() {

    // ── Deep link → start back-stack ─────────────────────────────────────────
    val activity = LocalContext.current as? Activity
    val startBackStack: List<AppRoute> = remember(activity) {
        DeepLinks.resolveBackStack(
            intent = activity?.intent,
            authenticated = false          // swap to `true` after session check
        )
    }

    // ── Back stack (Nav3-style) ───────────────────────────────────────────────
    val backStack = remember {
        mutableStateListOf<AppRoute>().apply {
            add(startBackStack.first())
        }
    }

    // Push thêm các entry còn lại của deep link (nếu có).
    LaunchedEffect(Unit) {
        startBackStack.drop(1).forEach { route -> backStack.add(route) }
    }

    // Route hiện tại là phần tử cuối cùng trong backStack
    val current = backStack.last()

    when (current) {
        // ── Login ─────────────────────────────────────────────────────────────
        is AppRoute.Login -> {
            LoginScreen(
                onLoginSuccess = {
                    backStack.popUpTo<AppRoute, AppRoute.Login>(inclusive = true)
                    backStack.add(AppRoute.Main)
                }
            )
        }

        // ── Main (Home / Explore / Library bottom-nav container) ─────────────
        is AppRoute.Main -> {
            HomeNavScreen(
                onNavigateToSetting = {
                    backStack.add(AppRoute.Setting)
                },
                onNavigateToDetail = { id, origin ->
                    backStack.add(AppRoute.Detail(id = id, origin = origin))
                }
            )
        }

        // ── Setting ───────────────────────────────────────────────────────────
        is AppRoute.Setting -> {
            SettingScreen(
                onBackClick = {
                    if (backStack.size > 1) backStack.removeLastOrNull()
                }
            )
        }

        // ── Detail (multi-param destination) ──────────────────────────────────
        is AppRoute.Detail -> {
            DetailScreen(
                id     = current.id,
                origin = current.origin,
                onBack = {
                    if (backStack.size > 1) backStack.removeLastOrNull()
                }
            )
        }
    }
}

