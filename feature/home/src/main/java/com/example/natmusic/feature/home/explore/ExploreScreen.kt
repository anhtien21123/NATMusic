package com.example.natmusic.feature.home.explore

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.core.common_ui.component.CategoryCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = koinViewModel(),
    onNavigateToCategory: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            is ExploreContract.SingleEvent.NavigateToCategory -> onNavigateToCategory(event.id)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Search",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(16.dp)
        )
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { viewModel.handleIntent(ExploreContract.Intent.Search(it)) },
            onSearch = {},
            active = false,
            onActiveChange = {},
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("What do you want to listen to?") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        ) {}
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Browse all",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.categories) { item ->
                CategoryCard(
                    title = item.title,
                    color = item.color,
                    onClick = { viewModel.handleIntent(ExploreContract.Intent.OpenCategory(item.id)) }
                )
            }
        }
    }
}

