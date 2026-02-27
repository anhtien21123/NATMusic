package com.example.natmusic.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.core.navigation.MainRoute
import com.example.natmusic.feature.home.explore.ExploreScreen
import com.example.natmusic.feature.home.home.HomeScreen
import com.example.natmusic.feature.home.library.LibraryScreen
import org.koin.androidx.compose.koinViewModel

// ── Bottom-nav descriptor ─────────────────────────────────────────────────────

private data class NavItem(
    val route: MainRoute,
    val label: String,
    val icon: ImageVector
)

private val NAV_ITEMS = listOf(
    NavItem(MainRoute.Home,    "Home",    Icons.Default.Home),
    NavItem(MainRoute.Explore, "Explore", Icons.Default.Explore),
    NavItem(MainRoute.Library, "Library", Icons.Default.LibraryMusic)
)

/**
 * HomeNavScreen — entry-point composable cho :feature:home.
 *
 * Ở đây mình **không** dùng Nav3 cho inner graph nữa để tránh ràng buộc
 * `NavKey` phức tạp, mà dùng MVI state + `when (state.currentTab)`:
 *
 * - ViewModel giữ `currentTab: MainRoute` trong State.
 * - BottomNav chỉ dispatch `OnTabSelected(route)` cho ViewModel.
 * - Phần content hiển thị `HomeScreen` / `ExploreScreen` / `LibraryScreen`
 *   theo `state.currentTab` — mỗi screen có ViewModel riêng nên state
 *   không bị mất khi chuyển tab.
 *
 * Điều này vẫn giữ đúng contract: navigation ra ngoài module (Setting, Detail)
 * đi qua SingleEvent + callback `onNavigateToSetting` / `onNavigateToDetail`.
 */
@Composable
fun HomeNavScreen(
    viewModel: HomeNavViewModel = koinViewModel(),
    onNavigateToSetting: () -> Unit,
    onNavigateToDetail: (id: String, origin: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Lifecycle-aware SingleEvent collection
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            HomeNavContract.SingleEvent.NavigateToSettings ->
                onNavigateToSetting()

            is HomeNavContract.SingleEvent.NavigateToDetail ->
                onNavigateToDetail(event.id, event.origin)
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NAV_ITEMS.forEach { item ->
                    NavigationBarItem(
                        selected = state.currentTab == item.route,
                        icon  = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        onClick = {
                            viewModel.handleIntent(
                                HomeNavContract.Intent.OnTabSelected(item.route)
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.currentTab) {
                MainRoute.Home -> {
                    HomeScreen(
                        onNavigateToDetail = { id ->
                            viewModel.handleIntent(
                                HomeNavContract.Intent.OnDetailRequested(id, "home")
                            )
                        }
                    )
                }
                MainRoute.Explore -> {
                    ExploreScreen(
                        onNavigateToCategory = { id ->
                            viewModel.handleIntent(
                                HomeNavContract.Intent.OnDetailRequested(id, "explore")
                            )
                        }
                    )
                }
                MainRoute.Library -> {
                    LibraryScreen(
                        onSettingsClick = {
                            viewModel.handleIntent(HomeNavContract.Intent.OnSettingsClicked)
                        }
                    )
                }
            }
        }
    }
}
