package com.example.natmusic.core.common_ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp

// ══════════════════════════════════════════════════════════════════════════════
//  Google Fonts provider
//  Requires:  implementation(libs.androidx.ui.text.google.fonts)
//  DFM note:  The provider downloads fonts lazily via Play Services.
//             In a Dynamic Feature environment the font is fetched from the
//             same Google Fonts cache as the base APK — no extra config needed.
// ══════════════════════════════════════════════════════════════════════════════

private val fontsProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs
)

// ── Poppins — headlines, display, titles ─────────────────────────────────────
private val poppins = GoogleFont("Poppins")

val PoppinsFontFamily = FontFamily(
    Font(googleFont = poppins, fontProvider = fontsProvider, weight = FontWeight.Normal),
    Font(googleFont = poppins, fontProvider = fontsProvider, weight = FontWeight.Medium),
    Font(googleFont = poppins, fontProvider = fontsProvider, weight = FontWeight.SemiBold),
    Font(googleFont = poppins, fontProvider = fontsProvider, weight = FontWeight.Bold)
)

// ── Inter — body, labels, captions ───────────────────────────────────────────
private val inter = GoogleFont("Inter")

val InterFontFamily = FontFamily(
    Font(googleFont = inter, fontProvider = fontsProvider, weight = FontWeight.Normal),
    Font(googleFont = inter, fontProvider = fontsProvider, weight = FontWeight.Medium),
    Font(googleFont = inter, fontProvider = fontsProvider, weight = FontWeight.SemiBold)
)

// ══════════════════════════════════════════════════════════════════════════════
//  NATMusic Typography — full M3 type scale
//
//  Design decisions:
//   • Display / Headline / Title → Poppins (strong, musical character)
//   • Body / Label               → Inter   (high readability at small sizes)
//   • Letter spacing adjusted for music-app density
//
//  Usage:  MaterialTheme.typography.headlineLarge  (standard M3 accessor)
// ══════════════════════════════════════════════════════════════════════════════

val NatTypography = Typography(
    // ── Display ──────────────────────────────────────────────────────────────
    displayLarge = TextStyle(
        fontFamily   = PoppinsFontFamily,
        fontWeight   = FontWeight.Bold,
        fontSize     = 57.sp,
        lineHeight   = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily   = PoppinsFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 45.sp,
        lineHeight   = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily   = PoppinsFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 36.sp,
        lineHeight   = 44.sp,
        letterSpacing = 0.sp
    ),

    // ── Headline ─────────────────────────────────────────────────────────────
    headlineLarge = TextStyle(
        fontFamily   = PoppinsFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 32.sp,
        lineHeight   = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily   = PoppinsFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 28.sp,
        lineHeight   = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily   = PoppinsFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 24.sp,
        lineHeight   = 32.sp,
        letterSpacing = 0.sp
    ),

    // ── Title ─────────────────────────────────────────────────────────────────
    titleLarge = TextStyle(
        fontFamily   = PoppinsFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 22.sp,
        lineHeight   = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily   = PoppinsFontFamily,
        fontWeight   = FontWeight.Medium,
        fontSize     = 16.sp,
        lineHeight   = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily   = PoppinsFontFamily,
        fontWeight   = FontWeight.Medium,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // ── Body ──────────────────────────────────────────────────────────────────
    bodyLarge = TextStyle(
        fontFamily   = InterFontFamily,
        fontWeight   = FontWeight.Normal,
        fontSize     = 16.sp,
        lineHeight   = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily   = InterFontFamily,
        fontWeight   = FontWeight.Normal,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily   = InterFontFamily,
        fontWeight   = FontWeight.Normal,
        fontSize     = 12.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // ── Label ─────────────────────────────────────────────────────────────────
    labelLarge = TextStyle(
        fontFamily   = InterFontFamily,
        fontWeight   = FontWeight.Medium,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily   = InterFontFamily,
        fontWeight   = FontWeight.Medium,
        fontSize     = 12.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily   = InterFontFamily,
        fontWeight   = FontWeight.Medium,
        fontSize     = 11.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.5.sp
    )
)
