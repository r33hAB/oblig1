/**
 * build.gradle.kts (prosjekt-nivå) - Konfigurasjon for hele prosjektet
 *
 * Denne filen definerer plugins som er tilgjengelige for alle moduler,
 * men som ikke anvendes på prosjektnivå (apply false).
 *
 * Hver modul (som :app) velger selv hvilke plugins den vil bruke
 * i sin egen build.gradle.kts fil.
 */

plugins {
    // Android Application plugin - for å bygge Android-apper
    alias(libs.plugins.android.application) apply false

    // Kotlin Android plugin - for Kotlin-støtte
    alias(libs.plugins.kotlin.android) apply false

    // KSP plugin - for kodegenerering brukt av Room
    alias(libs.plugins.ksp) apply false

    // Compose Compiler plugin - for Jetpack Compose
    alias(libs.plugins.compose.compiler) apply false
}
