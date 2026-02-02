/**
 * Type.kt - Typografi-definisjon for Jetpack Compose
 *
 * Denne filen definerer tekststiler som brukes i hele appen.
 * Vi bruker Material 3 standard typografi, men du kan tilpasse
 * fonter og størrelser her ved behov.
 *
 * MATERIAL 3 TYPOGRAFI-SKALA:
 * - displayLarge/Medium/Small: Store overskrifter
 * - headlineLarge/Medium/Small: Seksjonstitler
 * - titleLarge/Medium/Small: Komponent-titler
 * - bodyLarge/Medium/Small: Brødtekst
 * - labelLarge/Medium/Small: Knappetekst, etiketter
 */

package com.example.quizapp.ui.theme

import androidx.compose.material3.Typography

/**
 * Standard Material 3 typografi.
 *
 * Vi bruker standard verdier, men du kan tilpasse ved å:
 * - Endre fontFamily for custom fonter
 * - Justere fontSize, fontWeight, lineHeight
 *
 * Eksempel på tilpasning:
 * ```
 * val Typography = Typography(
 *     bodyLarge = TextStyle(
 *         fontFamily = FontFamily.Default,
 *         fontWeight = FontWeight.Normal,
 *         fontSize = 16.sp,
 *         lineHeight = 24.sp,
 *         letterSpacing = 0.5.sp
 *     )
 * )
 * ```
 */
val Typography = Typography()
