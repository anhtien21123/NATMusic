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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.natmusic.core.common_ui.component.LibraryItem
import com.example.natmusic.feature.home.domain.model.LibraryItemType

/**
 * Interface to group multiple UI actions for the Library screen.
 * Used when number of callbacks > 5.
 */
interface LibraryActions {
    fun onFilterClick(type: LibraryItemType?)
    fun onItemClick(id: String)
    fun onSettingsClick()
}

/**
 * Pure stateless UI for the Library feature.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryContent(
    state: LibraryContract.State,
    actions: LibraryActions,
    contentPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Library") },
                    actions = {
                        IconButton(onClick = actions::onSettingsClick) {
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
                        onClick = { actions.onFilterClick(null) },
                        label = { Text("All") }
                    )
                    FilterChip(
                        selected = state.selectedFilter == LibraryItemType.PLAYLIST,
                        onClick = { actions.onFilterClick(LibraryItemType.PLAYLIST) },
                        label = { Text("Playlists") }
                    )
                    FilterChip(
                        selected = state.selectedFilter == LibraryItemType.ARTIST,
                        onClick = { actions.onFilterClick(LibraryItemType.ARTIST) },
                        label = { Text("Artists") }
                    )
                    FilterChip(
                        selected = state.selectedFilter == LibraryItemType.ALBUM,
                        onClick = { actions.onFilterClick(LibraryItemType.ALBUM) },
                        label = { Text("Albums") }
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding())
        ) {
            items(state.items) { item ->
                LibraryItem(
                    title = item.title,
                    subtitle = item.subtitle,
                    imageUrl = item.imageUrl,
                    onClick = { actions.onItemClick(item.id) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun LibraryContentPreview() {
    LibraryContent(
        state = LibraryContract.State(),
        actions = object : LibraryActions {
            override fun onFilterClick(type: LibraryItemType?) {}
            override fun onItemClick(id: String) {}
            override fun onSettingsClick() {}
        }
    )
}
