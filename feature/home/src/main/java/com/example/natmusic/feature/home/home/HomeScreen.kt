package com.example.natmusic.feature.home.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.core.common_ui.component.MusicItemCard
import com.example.natmusic.core.common_ui.component.SectionTitle
import org.koin.androidx.compose.koinViewModel

/**
 * Home (feed) tab screen.
 *
 * • [koinViewModel] injects the ViewModel – replaces hiltViewModel().
 * • State collected via [collectAsStateWithLifecycle] (lifecycle-aware).
 * • SingleEvents collected via [collectSingleEvent] (also lifecycle-aware).
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToDetail: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // ── Lifecycle-aware SingleEvent collection ────────────────────────────────
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            is HomeContract.SingleEvent.NavigateToDetail -> onNavigateToDetail(event.id)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            Text(
                text = "Good evening 👋",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        item {
            SectionTitle(title = "Recently Played")
            LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
                items(state.recentItems) { item ->
                    MusicItemCard(
                        title = item.title,
                        subtitle = item.subtitle,
                        imageUrl = item.imageUrl,
                        onClick = { viewModel.handleIntent(HomeContract.Intent.OpenMusicItem(item.id)) }
                    )
                }
            }
        }

        item {
            SectionTitle(title = "Made For You")
            LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
                items(state.recommendedItems) { item ->
                    MusicItemCard(
                        title = item.title,
                        subtitle = item.subtitle,
                        imageUrl = item.imageUrl
                    )
                }
            }
        }

        item {
            SectionTitle(title = "Trending Now")
            LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
                items(state.trendingItems) { item ->
                    MusicItemCard(
                        title = item.title,
                        subtitle = item.subtitle,
                        imageUrl = item.imageUrl
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

