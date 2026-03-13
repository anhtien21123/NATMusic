package com.example.natmusic.core.common_ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * A stateless playback slider component for music and media controllers.
 * 
 * Provides fine-grained control over seeking operations by separating immediate drag 
 * feedback from the actual final seek intent. 
 *
 * @param progress Current playback progress value from 0.0 to 1.0.
 * @param onProgressChange Callback triggered immediately upon user interaction or drag. 
 * Use this to update the UI only (e.g., updating a local 'currentTime' text or 
 * smoothly updating the slider thumb position).
 * @param onProgressChangeFinished Callback triggered ONLY after the user releases the 
 * slider thumb. This is the optimal place to perform heavy 'seek' operations in the 
 * ViewModel or Player engine to avoid performance stuttering or unnecessary 
 * network/IO calls during continuous dragging.
 * @param colors Styling customized via [SliderColors]. Defaults to primary theme colors.
 * @param modifier standard Compose [Modifier] for layout adjustments.
 */
@Composable
fun PlaybackSlider(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    onProgressChangeFinished: (Float) -> Unit,
    modifier: Modifier = Modifier,
    colors: SliderColors = SliderDefaults.colors(
        thumbColor = MaterialTheme.colorScheme.primary,
        activeTrackColor = MaterialTheme.colorScheme.primary,
        inactiveTrackColor = MaterialTheme.natColors.textSecondary.copy(alpha = 0.3f),
        activeTickColor = MaterialTheme.colorScheme.primary,
        inactiveTickColor = MaterialTheme.colorScheme.primary
    )
) {
    Slider(
        value = progress.coerceIn(0f, 1f),
        onValueChange = onProgressChange,
        onValueChangeFinished = { 
            // We pass the current progress back as the final seek target.
            onProgressChangeFinished(progress) 
        },
        valueRange = 0f..1f,
        colors = colors,
        modifier = modifier
    )
}

@Preview
@Composable
private fun PlaybackSliderPreview() {
    MaterialTheme {
        PlaybackSlider(
            progress = 0.45f,
            onProgressChange = {},
            onProgressChangeFinished = {}
        )
    }
}
