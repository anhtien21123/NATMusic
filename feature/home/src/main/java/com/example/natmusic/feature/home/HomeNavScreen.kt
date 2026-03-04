package com.example.natmusic.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.core.navigation.MainRoute
import com.example.natmusic.feature.home.explore.ExploreScreen
import com.example.natmusic.feature.home.home.HomeScreen
import com.example.natmusic.feature.home.library.LibraryScreen
import org.koin.androidx.compose.koinViewModel
import com.example.natmusic.core.common_ui.spacing

// ── Bottom-nav descriptor ─────────────────────────────────────────────────────

private data class NavItem(
    val route: MainRoute,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val NAV_ITEMS = listOf(
    NavItem(MainRoute.Home,    "Home",    Icons.Default.Home,         Icons.Outlined.Home),
    NavItem(MainRoute.Explore, "Search",  Icons.Default.Search,       Icons.Outlined.Search),
    NavItem(MainRoute.Library, "Library", Icons.Default.LibraryMusic, Icons.Outlined.LibraryMusic)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val spacing = MaterialTheme.spacing
        
        // --- Screen Content with Tab Transitions ---
        AnimatedContent(
            targetState = state.currentTab,
            transitionSpec = {
                val initialIndex = NAV_ITEMS.indexOfFirst { it.route == initialState }
                val targetIndex = NAV_ITEMS.indexOfFirst { it.route == targetState }

                if (targetIndex > initialIndex) {
                    // Slide in from right, slide out to left
                    (slideInHorizontally { width -> width } + fadeIn())
                        .togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
                } else {
                    // Slide in from left, slide out to right
                    (slideInHorizontally { width -> -width } + fadeIn())
                        .togetherWith(slideOutHorizontally { width -> width } + fadeOut())
                }
            },
            label = "TabTransition"
        ) { currentTab ->
            Box(modifier = Modifier.fillMaxSize()) {
                when (currentTab) {
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

        // --- Custom Bottom Navigation Bar ---
        CustomBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = spacing.lg) // Lift it from bottom using design system lg (24dp)
                .navigationBarsPadding(),
            currentTab = state.currentTab,
            onTabSelected = { route ->
                viewModel.handleIntent(HomeNavContract.Intent.OnTabSelected(route))
            }
        )
    }
}

@Composable
private fun CustomBottomBar(
    modifier: Modifier = Modifier,
    currentTab: MainRoute,
    onTabSelected: (MainRoute) -> Unit
) {
    val spacing = MaterialTheme.spacing
    
    Row(
        modifier = modifier
            .padding(horizontal = spacing.lg)
            .height(spacing.xxxl + spacing.sm) // Using tokens to reach ~72dp if needed, but 64dp is standard xxxl
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f), // Using surface token instead of hardcoded
                shape = CircleShape
            )
            .padding(horizontal = spacing.sm),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NAV_ITEMS.forEach { item ->
            val isSelected = currentTab == item.route
            
            val interactionSource = remember { MutableInteractionSource() }

            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onTabSelected(item.route) }
                    )
                    .padding(horizontal = spacing.lg20, vertical = spacing.md12),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                    contentDescription = item.label,
                    tint = if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(spacing.lg)
                )
                
                AnimatedVisibility(visible = isSelected) {
                    Row {
                        Spacer(modifier = Modifier.width(spacing.sm))
                        Text(
                            text = item.label,
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
