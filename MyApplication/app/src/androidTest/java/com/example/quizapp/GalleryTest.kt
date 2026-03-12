package com.example.quizapp

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test 3: Galleri add/delete test.
 *
 * Verifiserer at antall registrerte bilder er korrekt etter
 * tillegging og sletting av en oppføring.
 * Bruker Intent Stubbing for å simulere bildevalg fra telefonen
 * uten brukerinteraksjon.
 */
@RunWith(AndroidJUnit4::class)
class GalleryTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<GalleriActivity>()

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    /**
     * Tester at antall galleri-elementer er korrekt etter tillegging og sletting.
     *
     * Trinn:
     * 1. GalleriActivity starter med 3 innebygde bilder (Katt, Hund, Kanin).
     * 2. Verifiser at antallet er 3.
     * 3. Stub ACTION_OPEN_DOCUMENT for å returnere et test-bilde (resource URI).
     * 4. Klikk FAB (+) for å legge til nytt bilde.
     * 5. Skriv inn navnet "TestDyr" i dialogen og bekreft.
     * 6. Verifiser at antallet er 4.
     * 7. Klikk slett-knappen for "TestDyr" og bekreft sletting.
     * 8. Verifiser at antallet er tilbake til 3.
     */
    @Test
    fun gallery_item_count_correct_after_add_and_delete() {
        val viewModel = composeTestRule.activity.viewModel

        // Vent til galleriet er lastet med innebygde bilder
        composeTestRule.waitUntil(5000) {
            viewModel.uiState.value.items.size >= 3
        }

        val initialCount = viewModel.uiState.value.items.size
        assertEquals("Skal ha 3 innebygde bilder ved start", 3, initialCount)

        // --- LEGG TIL nytt bilde med Intent Stubbing ---
        val testUri = Uri.parse("android.resource://com.example.quizapp/${R.drawable.dyr1}")
        val resultData = Intent().apply { data = testUri }
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

        // Stub ACTION_OPEN_DOCUMENT slik at bildevelgeren returnerer test-URI
        Intents.intending(hasAction(Intent.ACTION_OPEN_DOCUMENT)).respondWith(result)

        // Klikk FAB for å åpne bildevelgeren
        composeTestRule.onNodeWithContentDescription("Legg til bilde").performClick()

        // Vent til "gi navn"-dialogen vises
        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithText("Gi bildet et navn")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Skriv inn navn og bekreft
        composeTestRule.onNode(hasSetTextAction()).performTextInput("TestDyr")
        composeTestRule.onNodeWithText("Bekreft").performClick()

        // Vent til elementet er lagt til i Room
        composeTestRule.waitUntil(5000) {
            viewModel.uiState.value.items.size == initialCount + 1
        }

        assertEquals(
            "Antall skal være ${initialCount + 1} etter tillegging",
            initialCount + 1,
            viewModel.uiState.value.items.size
        )

        // --- SLETT "TestDyr" ---
        // Klikk slett-knappen som har testTag "slett_TestDyr"
        composeTestRule.onNodeWithTag("slett_TestDyr").performClick()

        // Vent til bekreftelsesdialogen vises
        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithText("Slett bilde?")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Bekreft sletting
        composeTestRule.onNodeWithText("Slett").performClick()

        // Vent til elementet er slettet
        composeTestRule.waitUntil(5000) {
            viewModel.uiState.value.items.size == initialCount
        }

        assertEquals(
            "Antall skal være $initialCount etter sletting",
            initialCount,
            viewModel.uiState.value.items.size
        )
    }
}
