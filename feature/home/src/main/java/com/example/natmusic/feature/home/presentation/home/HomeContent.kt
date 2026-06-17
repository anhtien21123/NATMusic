package com.example.natmusic.feature.home.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.natmusic.core.common_ui.component.MusicItemCard
import com.example.natmusic.core.common_ui.component.SectionTitle
import com.example.natmusic.core.common_ui.spacing

/**
 * Pure stateless UI for the Home feed tab.
 *
 * ── Architectural contract ────────────────────────────────────────────────────
 *  • NO ViewModel, NO Contract, NO playback logic.
 *  • Single callback: [onMusicItemClick] — fires the track ID upward.
 *    [HomeScreen] maps it to [HomeContract.Intent.OpenMusicItem]; the ViewModel
 *    starts playback via [MusicPlayerHandler]; [MainViewModel] reflects the change
 *    in [MainContract.State] and the MiniPlayer appears automatically.
 *  • [contentPadding] is supplied by the shell ([HomeNavScreen]) so this composable
 *    never hard-codes how much space to leave for the MiniPlayer or bottom bar.
 *
 * ── What was removed ──────────────────────────────────────────────────────────
 *  The embedded MiniPlayer Card, [onPlayPauseClick], [onNextClick],
 *  [onPreviousClick], and [onSeek] have ALL been deleted. The MiniPlayer is
 *  now a persistent shell-level component rendered by [HomeNavContent]; it
 *  overlays every tab screen without any tab knowing about it.
 *
 * @param state            Feed lists to display.
 * @param onMusicItemClick Callback when the user taps any music item card.
 * @param contentPadding   Bottom padding injected by the shell to clear the
 *                         MiniPlayer + bottom nav bar.
 */
@Composable
fun HomeContent(
    state: HomeContract.State,
    onMusicItemClick: (id: String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding
        ) {

            // ── Greeting ──────────────────────────────────────────────────────
            item {
                Text(
                    text = "Good evening 👋",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(
                        horizontal = spacing.md,
                        vertical   = spacing.md12
                    )
                )
            }

            // ── Recently Played ───────────────────────────────────────────────
            item {
                SectionTitle(title = "Recently Played")
                LazyRow(contentPadding = PaddingValues(horizontal = spacing.sm)) {
                    items(state.recentItems) { item ->
                        MusicItemCard(
                            title    = item.title,
                            subtitle = item.subtitle,
                            imageUrl = item.imageUrl,
                            onClick  = { onMusicItemClick(item.id) }
                        )
                    }
                }
            }

            // ── Made For You ──────────────────────────────────────────────────
            item {
                SectionTitle(title = "Made For You")
                LazyRow(contentPadding = PaddingValues(horizontal = spacing.sm)) {
                    items(state.recommendedItems) { item ->
                        MusicItemCard(
                            title    = item.title,
                            subtitle = item.subtitle,
                            imageUrl = item.imageUrl,
                            onClick  = { onMusicItemClick(item.id) }
                        )
                    }
                }
            }

            // ── Trending Now ──────────────────────────────────────────────────
            item {
                SectionTitle(title = "Trending Now")
                LazyRow(contentPadding = PaddingValues(horizontal = spacing.sm)) {
                    items(state.trendingItems) { item ->
                        MusicItemCard(
                            title    = item.title,
                            subtitle = item.subtitle,
                            imageUrl = item.imageUrl,
                            onClick  = { onMusicItemClick(item.id) }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(spacing.xl)) }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "HomeContent – Empty", showBackground = true)
@Composable
private fun HomeContentEmptyPreview() {
    MaterialTheme {
        HomeContent(
            state            = HomeContract.State(),
            onMusicItemClick = {}
        )
    }
}

@Preview(name = "HomeContent – With data", showBackground = true)
@Composable
private fun HomeContentWithDataPreview() {
    val previewItems = listOf(
        MusicItemUi("1", "Midnight Rain", "Taylor Swift", "https://picsum.photos/seed/1/300/300"),
        MusicItemUi("2", "As It Was", "Harry Styles", "https://picsum.photos/seed/2/300/300")
    )
    MaterialTheme {
        HomeContent(
            state = HomeContract.State(
                recentItems      = previewItems,
                recommendedItems = previewItems,
                trendingItems    = previewItems
            ),
            onMusicItemClick = {}
        )
    }
}
