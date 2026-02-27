package com.example.natmusic.core.common_ui

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.ui.graphics.vector.ImageVector

// ══════════════════════════════════════════════════════════════════════════════
//  NatIcons — NATMusic icon catalog
//
//  All icons live in ONE place. Feature modules import only this object —
//  they never depend on raw R.drawable or material-icons paths directly.
//
//  Two access styles:
//
//  1. ImageVector (preferred in Compose)
//     Icon(imageVector = NatIcons.Play, contentDescription = "Play")
//
//  2. DrawableRes (@DrawableRes Int — for XML layouts, notifications,
//     RemoteViews, and legacy View-based code)
//     Icon(painter = painterResource(NatIcons.Res.play), contentDescription = null)
//
//  Where do the files live?
//    ImageVector → material-icons-extended (no file, pure Kotlin)
//    DrawableRes → :core:common_ui/src/main/res/drawable/nat_ic_*.xml
//                  (already in the same module, so no extra dependency)
//
//  How to add a new icon:
//    1. Drop  nat_ic_your_icon.xml  into  :core:common_ui/res/drawable/
//    2. Add a line in [NatIcons.Res]:  val yourIcon = R.drawable.nat_ic_your_icon
//    3. (optional) Add an [ImageVector] alias in [NatIcons] if a Material
//       Icons equivalent exists.
// ══════════════════════════════════════════════════════════════════════════════

object NatIcons {

    // ── Navigation ────────────────────────────────────────────────────────────
    val Back: ImageVector    = Icons.AutoMirrored.Filled.ArrowBack
    val Home: ImageVector    = Icons.Filled.Home
    val Explore: ImageVector = Icons.Filled.Explore
    val Library: ImageVector = Icons.Filled.LibraryMusic
    val Setting: ImageVector = Icons.Filled.Settings

    // ── Playback controls ─────────────────────────────────────────────────────
    val Play: ImageVector     = Icons.Filled.PlayArrow
    val Pause: ImageVector    = Icons.Filled.Pause
    val Next: ImageVector     = Icons.Filled.SkipNext
    val Previous: ImageVector = Icons.Filled.SkipPrevious
    val Shuffle: ImageVector  = Icons.Filled.Shuffle
    val Repeat: ImageVector   = Icons.Filled.Repeat

    // ── Actions ───────────────────────────────────────────────────────────────
    val Favorite: ImageVector       = Icons.Filled.Favorite
    val FavoriteBorder: ImageVector = Icons.Filled.FavoriteBorder
    val MoreVert: ImageVector       = Icons.Filled.MoreVert

    // ══════════════════════════════════════════════════════════════════════════
    //  Res — DrawableRes aliases for XML / legacy / notification use cases
    //
    //  Usage:
    //    val painter = painterResource(NatIcons.Res.play)
    //    Icon(painter = painter, contentDescription = null)
    //
    //    // In a notification RemoteViews (no Compose):
    //    remoteViews.setImageViewResource(R.id.btn_play, NatIcons.Res.play)
    // ══════════════════════════════════════════════════════════════════════════
    object Res {
        // Navigation
        @DrawableRes val back: Int    = R.drawable.ic_back
        @DrawableRes val home: Int    = R.drawable.ic_home
        @DrawableRes val explore: Int = R.drawable.ic_explore
        @DrawableRes val library: Int = R.drawable.ic_library
        @DrawableRes val setting: Int = R.drawable.ic_setting

        // Playback
        @DrawableRes val play: Int     = R.drawable.ic_play
        @DrawableRes val pause: Int    = R.drawable.ic_pause
        @DrawableRes val next: Int     = R.drawable.ic_next
        @DrawableRes val previous: Int = R.drawable.ic_previous
        @DrawableRes val shuffle: Int  = R.drawable.ic_shuffle
        @DrawableRes val repeat: Int   = R.drawable.ic_repeat

        // Actions
        @DrawableRes val favorite: Int       = R.drawable.ic_favorite
        @DrawableRes val favoriteBorder: Int = R.drawable.ic_favorite_border
        @DrawableRes val moreVert: Int       = R.drawable.ic_more_vert
    }
}

