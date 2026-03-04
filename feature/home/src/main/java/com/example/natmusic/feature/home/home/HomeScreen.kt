package com.example.natmusic.feature.home.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.core.common_ui.component.MusicItemCard
import com.example.natmusic.core.common_ui.component.SectionTitle
import org.koin.androidx.compose.koinViewModel
import com.example.natmusic.core.common_ui.spacing

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

    val spacing = MaterialTheme.spacing

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = spacing.xxxl + spacing.xl + 80.dp) // Clearance for floating bottom nav + mini player
        ) {
        item {
            Text(
                text = "Good evening 👋",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = spacing.md, vertical = spacing.md12)
            )
        }

        item {
            SectionTitle(title = "Recently Played")
            LazyRow(contentPadding = PaddingValues(horizontal = spacing.sm)) {
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
            LazyRow(contentPadding = PaddingValues(horizontal = spacing.sm)) {
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
            LazyRow(contentPadding = PaddingValues(horizontal = spacing.sm)) {
                items(state.trendingItems) { item ->
                    MusicItemCard(
                        title = item.title,
                        subtitle = item.subtitle,
                        imageUrl = item.imageUrl
                    )
                }
            }
        }

            item { Spacer(modifier = Modifier.height(spacing.xl)) }
        }

        // ── Mini Player ───────────────────────────────────────────────────────
        if (state.currentMediaId != null) {
            val currentItem = state.recentItems.find { it.id == state.currentMediaId }
                ?: state.recommendedItems.find { it.id == state.currentMediaId }
                ?: state.trendingItems.find { it.id == state.currentMediaId }

            currentItem?.let { item ->
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = spacing.md)
                        .padding(bottom = spacing.xxxl + spacing.md) // Above bottom nav
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
                    ),
                    shape = RoundedCornerShape(spacing.md)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(spacing.sm)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(spacing.xs))
                        )
                        
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = spacing.sm)
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleSmall,
                                maxLines = 1
                            )
                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )
                        }

                        IconButton(onClick = { viewModel.handleIntent(HomeContract.Intent.Previous) }) {
                            Icon(Icons.Default.SkipPrevious, contentDescription = "Previous")
                        }
                        
                        IconButton(onClick = { viewModel.handleIntent(HomeContract.Intent.PlayPause) }) {
                            Icon(
                                if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause"
                            )
                        }

                        IconButton(onClick = { viewModel.handleIntent(HomeContract.Intent.Next) }) {
                            Icon(Icons.Default.SkipNext, contentDescription = "Next")
                        }
                    }
                }
            }
        }
    }
}

