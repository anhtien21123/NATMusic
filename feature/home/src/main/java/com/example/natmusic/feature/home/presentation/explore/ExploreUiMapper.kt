package com.example.natmusic.feature.home.explore

import androidx.compose.ui.graphics.Color
import com.example.natmusic.feature.home.domain.model.ExploreCategory

data class ExploreItemUi(
    val id: String,
    val title: String,
    val color: Color
)

fun ExploreCategory.toUi(): ExploreItemUi = ExploreItemUi(
    id = id,
    title = name,
    color = Color(colorArgb)
)
