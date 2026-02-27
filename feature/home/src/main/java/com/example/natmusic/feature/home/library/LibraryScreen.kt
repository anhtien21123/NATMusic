package com.example.natmusic.feature.home.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.core.common_ui.component.LibraryItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = koinViewModel(),
    onSettingsClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // ── Lifecycle-aware SingleEvent collection ────────────────────────────────
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            is LibraryContract.SingleEvent.NavigateToSettings -> onSettingsClick()
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Library") },
                    actions = {
                        IconButton(onClick = onSettingsClick) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = state.selectedFilter == null,
                        onClick = { viewModel.handleIntent(LibraryContract.Intent.FilterByType(null)) },
                        label = { Text("All") }
                    )
                    FilterChip(
                        selected = state.selectedFilter == MediaType.PLAYLIST,
                        onClick = { viewModel.handleIntent(LibraryContract.Intent.FilterByType(MediaType.PLAYLIST)) },
                        label = { Text("Playlists") }
                    )
                    FilterChip(
                        selected = state.selectedFilter == MediaType.ARTIST,
                        onClick = { viewModel.handleIntent(LibraryContract.Intent.FilterByType(MediaType.ARTIST)) },
                        label = { Text("Artists") }
                    )
                    FilterChip(
                        selected = state.selectedFilter == MediaType.ALBUM,
                        onClick = { viewModel.handleIntent(LibraryContract.Intent.FilterByType(MediaType.ALBUM)) },
                        label = { Text("Albums") }
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(state.items) { item ->
                LibraryItem(
                    title = item.title,
                    subtitle = item.subtitle,
                    imageUrl = item.imageUrl,
                    onClick = { viewModel.handleIntent(LibraryContract.Intent.OpenItem(item.id)) }
                )
            }
        }
    }
}

