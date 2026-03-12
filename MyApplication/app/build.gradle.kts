/**
 * build.gradle.kts (app-modul) - Byggekonfigurasjon for Quiz-appen
 *
 * Denne filen konfigurerer hvordan appen skal bygges, inkludert:
 * - Kotlin-støtte for moderne Android-utvikling
 * - Jetpack Compose for deklarativ UI
 * - Nødvendige avhengigheter for appen
 *
 * VIKTIGE VALG:
 * - Vi bruker Kotlin 2.0 med den nye Compose Compiler-plugin
 * - Compose BOM (Bill of Materials) sikrer kompatible versjoner
 * - Coil brukes for enkel bildehåndtering i Compose
 */

plugins {
    // Android Application plugin - nødvendig for å bygge en Android-app
    alias(libs.plugins.android.application)

    // Kotlin Android plugin - gir Kotlin-støtte
    alias(libs.plugins.kotlin.android)

    // KSP - required for Room code generation
    alias(libs.plugins.ksp)

    // Compose Compiler plugin - nødvendig for Jetpack Compose fra Kotlin 2.0
    alias(libs.plugins.compose.compiler)
}

android {
    // Unikt navnerom for appen (brukes for R-klassen og BuildConfig)
    namespace = "com.example.quizapp"

    // API-nivå som appen kompileres mot (nyeste stabile)
    compileSdk = 35

    defaultConfig {
        // Unik identifikator for appen i Play Store
        applicationId = "com.example.quizapp"

        // Minimum Android-versjon som støttes (Android 7.0 Nougat)
        // Valgt for å støtte de fleste enheter mens vi får moderne APIer
        minSdk = 24

        // Target SDK - appen er testet mot denne versjonen
        targetSdk = 35

        // Versjonskode og -navn for oppdateringer
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Vektor-drawable støtte for eldre Android-versjoner
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            // Deaktiverer minifisering for enklere feilsøking i oblig
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Java 11 for moderne språkfunksjoner
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Kotlin JVM target må matche Java-versjonen
    kotlinOptions {
        jvmTarget = "11"
    }

    // Aktiverer Jetpack Compose
    buildFeatures {
        compose = true
    }
}

dependencies {
    // === CORE ANDROID ===
    // Kotlin extensions for Android - gir kortere og mer idiomatisk kode
    implementation(libs.androidx.core.ktx)

    // AppCompat - bakoverkompatibilitet for moderne UI-funksjoner
    implementation(libs.androidx.appcompat)

    // Material Design komponenter for XML-layouts (brukes i hovedmenyen)
    implementation(libs.material)

    // Activity KTX - moderne Activity-funksjoner inkludert result API
    implementation(libs.androidx.activity)

    // ConstraintLayout for XML-basert hovedmeny
    implementation(libs.androidx.constraintlayout)

    // Lifecycle ViewModel for future screen state handling
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // === JETPACK COMPOSE ===
    // BOM (Bill of Materials) sikrer at alle Compose-biblioteker har kompatible versjoner
    implementation(platform(libs.androidx.compose.bom))

    // Activity Compose - bro mellom Activity og Compose
    implementation(libs.androidx.activity.compose)

    // Compose UI - kjernebiblioteket for Compose
    implementation(libs.androidx.compose.ui)

    // Compose Graphics - tegning og grafikk
    implementation(libs.androidx.compose.ui.graphics)

    // Compose Preview - forhåndsvisning i Android Studio
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Material 3 - moderne Material Design komponenter for Compose
    implementation(libs.androidx.compose.material3)

    // Debug-verktøy for Compose (kun i debug-builds)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // === BILDEHÅNDTERING ===
    // Coil - moderne, Kotlin-first bildebibliotek
    // Valgt fremfor Glide/Picasso fordi:
    // - Native Compose-støtte
    // - Kotlin coroutines
    // - Mindre og raskere
    implementation(libs.coil.compose)

    // === ROOM ===
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // === TESTING ===
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.intents)

    // Compose UI testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
