/**
 * settings.gradle.kts - Prosjektinnstillinger
 *
 * Denne filen konfigurerer:
 * - Plugin repositories: Hvor Gradle finner plugins
 * - Dependency repositories: Hvor Gradle finner biblioteker
 * - Hvilke moduler som inngår i prosjektet
 */

pluginManagement {
    repositories {
        // Google's Maven repository for Android plugins
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        // Maven Central for de fleste andre biblioteker
        mavenCentral()
        // Gradle Plugin Portal for Gradle-spesifikke plugins
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // Fail hvis moduler prøver å definere egne repositories
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Prosjektets navn
rootProject.name = "QuizApp"

// Inkluderer app-modulen
include(":app")
