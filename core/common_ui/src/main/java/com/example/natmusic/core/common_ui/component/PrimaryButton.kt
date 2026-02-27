package com.example.natmusic.core.common_ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.natmusic.core.common_ui.NATMusicTheme
import com.example.natmusic.core.common_ui.natColors

// ══════════════════════════════════════════════════════════════════════════════
//  PrimaryButton — NATMusic's primary call-to-action button
//
//  Variants:
//   • Filled   (default)  — solid background, highest emphasis
//   • Outlined            — transparent bg with border, medium emphasis
//   • Text                — no background or border, lowest emphasis
//
//  Features:
//   • Loading state — replaces label with a spinner + blocks interaction
//   • Leading icon  — optional icon to the left of the label
//   • Full-width    — stretches to parent width by default
//   • Accent color  — optional flag to use brand accent instead of primary
//
//  Usage:
//   PrimaryButton(text = "Play All", onClick = { … })
//   PrimaryButton(text = "Follow", variant = ButtonVariant.Outlined, onClick = { … })
//   PrimaryButton(text = "Logging in…", isLoading = true, onClick = {})
// ══════════════════════════════════════════════════════════════════════════════

enum class ButtonVariant { Filled, Outlined, Text }

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Filled,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    useAccentColor: Boolean = false,
    leadingIcon: ImageVector? = null
) {
    val isEnabled = enabled && !isLoading
    val resolvedModifier = modifier
        .fillMaxWidth()
        .height(50.dp)
        .animateContentSize()
        .semantics {
            if (isLoading) contentDescription = "$text, loading"
        }

    when (variant) {
        ButtonVariant.Filled -> FilledPrimaryButton(
            text          = text,
            onClick       = onClick,
            modifier      = resolvedModifier,
            isLoading     = isLoading,
            enabled       = isEnabled,
            useAccentColor = useAccentColor,
            leadingIcon   = leadingIcon
        )
        ButtonVariant.Outlined -> OutlinedPrimaryButton(
            text        = text,
            onClick     = onClick,
            modifier    = resolvedModifier,
            isLoading   = isLoading,
            enabled     = isEnabled,
            leadingIcon = leadingIcon
        )
        ButtonVariant.Text -> TextPrimaryButton(
            text        = text,
            onClick     = onClick,
            modifier    = resolvedModifier,
            isLoading   = isLoading,
            enabled     = isEnabled,
            leadingIcon = leadingIcon
        )
    }
}

// ── Filled ─────────────────────────────────────────────────────────────────────

@Composable
private fun FilledPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    isLoading: Boolean,
    enabled: Boolean,
    useAccentColor: Boolean,
    leadingIcon: ImageVector?
) {
    val containerColor = if (useAccentColor) MaterialTheme.natColors.accent
                         else MaterialTheme.colorScheme.primary
    val contentColor   = if (useAccentColor) Color.White
                         else MaterialTheme.colorScheme.onPrimary

    Button(
        onClick  = onClick,
        modifier = modifier,
        enabled  = enabled,
        shape    = MaterialTheme.shapes.small,
        colors   = ButtonDefaults.buttonColors(
            containerColor         = containerColor,
            contentColor           = contentColor,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor   = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        ButtonContent(text = text, isLoading = isLoading, leadingIcon = leadingIcon,
                      spinnerColor = contentColor)
    }
}

// ── Outlined ───────────────────────────────────────────────────────────────────

@Composable
private fun OutlinedPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    isLoading: Boolean,
    enabled: Boolean,
    leadingIcon: ImageVector?
) {
    OutlinedButton(
        onClick  = onClick,
        modifier = modifier,
        enabled  = enabled,
        shape    = MaterialTheme.shapes.small,
        border   = BorderStroke(
            width = 1.5.dp,
            color = if (enabled) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline
        ),
        colors  = ButtonDefaults.outlinedButtonColors(
            contentColor         = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        ButtonContent(text = text, isLoading = isLoading, leadingIcon = leadingIcon,
                      spinnerColor = MaterialTheme.colorScheme.primary)
    }
}

// ── Text ───────────────────────────────────────────────────────────────────────

@Composable
private fun TextPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    isLoading: Boolean,
    enabled: Boolean,
    leadingIcon: ImageVector?
) {
    TextButton(
        onClick  = onClick,
        modifier = modifier,
        enabled  = enabled,
        shape    = MaterialTheme.shapes.small,
        colors   = ButtonDefaults.textButtonColors(
            contentColor         = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        ButtonContent(text = text, isLoading = isLoading, leadingIcon = leadingIcon,
                      spinnerColor = MaterialTheme.colorScheme.primary)
    }
}

// ── Shared content ─────────────────────────────────────────────────────────────

@Composable
private fun ButtonContent(
    text: String,
    isLoading: Boolean,
    leadingIcon: ImageVector?,
    spinnerColor: Color
) {
    Box(contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier  = Modifier.size(20.dp),
                color     = spinnerColor,
                strokeWidth = 2.5.dp
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) {
                    androidx.compose.material3.Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(text = text, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

// ── Previews ───────────────────────────────────────────────────────────────────

@Preview(name = "Filled — idle", showBackground = true)
@Composable
private fun FilledIdlePreview() {
    NATMusicTheme { PrimaryButton("Play All", onClick = {}) }
}

@Preview(name = "Filled — loading", showBackground = true)
@Composable
private fun FilledLoadingPreview() {
    NATMusicTheme { PrimaryButton("Logging in…", onClick = {}, isLoading = true) }
}

@Preview(name = "Outlined", showBackground = true)
@Composable
private fun OutlinedPreview() {
    NATMusicTheme { PrimaryButton("Follow", onClick = {}, variant = ButtonVariant.Outlined) }
}

@Preview(name = "Text", showBackground = true)
@Composable
private fun TextPreview() {
    NATMusicTheme { PrimaryButton("See All", onClick = {}, variant = ButtonVariant.Text) }
}

@Preview(name = "Accent + Disabled", showBackground = true)
@Composable
private fun AccentDisabledPreview() {
    NATMusicTheme {
        PrimaryButton("Shuffle Play", onClick = {}, useAccentColor = true, enabled = false)
    }
}

