/**
 * Theme.kt - Tema-definisjon for Jetpack Compose
 *
 * Denne filen definerer det visuelle temaet for alle Compose-skjermer i appen.
 *
 * DESIGNVALG:
 * - Material 3 (Material You) brukes for moderne utseende
 * - Dynamiske farger støttes på Android 12+ for personlig tilpasning
 * - Lys og mørk modus støttes automatisk
 *
 * FARGEVALG:
 * - Primærfarge: Blå - trygg og profesjonell
 * - Sekundærfarge: Lilla - kreativ og leken
 * - Error-farge: Rød - standard for feil
 */

package com.example.quizapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Farger for lyst tema.
 *
 * Disse fargene brukes når systemet er i lys modus
 * eller dynamiske farger ikke er tilgjengelig.
 */
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2),           // Blå - hovedfarge
    onPrimary = Color.White,               // Tekst på primærfarge
    primaryContainer = Color(0xFFBBDEFB),  // Lyseblå container
    onPrimaryContainer = Color(0xFF004BA0), // Tekst på container

    secondary = Color(0xFF7B1FA2),         // Lilla - sekundærfarge
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE1BEE7),
    onSecondaryContainer = Color(0xFF4A0072),

    tertiary = Color(0xFF00796B),          // Teal - tertiær
    onTertiary = Color.White,

    error = Color(0xFFD32F2F),             // Rød for feil
    onError = Color.White,

    background = Color(0xFFFAFAFA),        // Lys bakgrunn
    onBackground = Color(0xFF1C1B1F),      // Mørk tekst

    surface = Color.White,                  // Overflate (kort, dialog)
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F)
)

/**
 * Farger for mørkt tema.
 *
 * Disse fargene brukes når systemet er i mørk modus.
 * Fargene er justert for god lesbarhet på mørk bakgrunn.
 */
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),           // Lysere blå for kontrast
    onPrimary = Color(0xFF003C8F),
    primaryContainer = Color(0xFF1565C0),
    onPrimaryContainer = Color(0xFFD1E4FF),

    secondary = Color(0xFFCE93D8),
    onSecondary = Color(0xFF38006B),
    secondaryContainer = Color(0xFF7B1FA2),
    onSecondaryContainer = Color(0xFFF3E5F5),

    tertiary = Color(0xFF80CBC4),
    onTertiary = Color(0xFF00363D),

    error = Color(0xFFEF9A9A),
    onError = Color(0xFF690005),

    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),

    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0)
)

/**
 * Hovedtema for Quiz-appen.
 *
 * Denne composable-funksjonen setter opp farger, typografi og former
 * for alle barn-composables.
 *
 * @param darkTheme Om mørkt tema skal brukes (standard: systeminnstilling)
 * @param dynamicColor Om dynamiske farger skal brukes (Android 12+)
 * @param content Innholdet som skal bruke dette temaet
 *
 * HVORDAN DET FUNGERER:
 * - MaterialTheme gir tilgang til farger via MaterialTheme.colorScheme
 * - Alle Material 3 komponenter arver disse fargene automatisk
 * - Custom komponenter kan bruke fargene via MaterialTheme.colorScheme.*
 */
@Composable
fun QuizAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Velg fargeskjema basert på innstillinger
    val colorScheme = when {
        // Dynamiske farger på Android 12+
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        // Manuelt definerte farger
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Oppdater statusbar-farge for å matche temaet
    // Bruker @Suppress for å unngå deprecation-varsel på eldre API-er
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // Sett opp Material Theme med valgt fargeskjema
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,  // Standard Material 3 typografi
        content = content
    )
}
