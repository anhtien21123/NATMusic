package com.example.natmusic.feature.home.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.natmusic.core.common_ui.component.CategoryCard
import com.example.natmusic.core.common_ui.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreContent(
    state: ExploreContract.State,
    onSearchQueryChange: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Search",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(spacing.md)
        )
        SearchBar(
            query = state.searchQuery,
            onQueryChange = onSearchQueryChange,
            onSearch = {},
            active = false,
            onActiveChange = {},
            modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.md),
            placeholder = { Text("What do you want to listen to?") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        ) {}
        Spacer(modifier = Modifier.height(spacing.md))
        Text(
            text = "Browse all",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = spacing.md, vertical = spacing.sm)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start  = spacing.md,
                end    = spacing.md,
                bottom = contentPadding.calculateBottomPadding()
            ),
            horizontalArrangement = Arrangement.spacedBy(spacing.md12),
            verticalArrangement = Arrangement.spacedBy(spacing.md12)
        ) {
            items(state.categories) { item ->
                CategoryCard(
                    title = item.title,
                    color = item.color,
                    onClick = { onCategoryClick(item.id) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ExploreContentPreview() {
    MaterialTheme {
        ExploreContent(
            state = ExploreContract.State(
                categories = listOf(
                    ExploreItemUi("1", "Pop", Color(0xFFEF5350)),
                    ExploreItemUi("2", "Rock", Color(0xFFAB47BC)),
                    ExploreItemUi("3", "Hip-Hop", Color(0xFF42A5F5)),
                    ExploreItemUi("4", "Jazz", Color(0xFF26A69A))
                )
            ),
            onSearchQueryChange = {},
            onCategoryClick = {}
        )
    }
}
