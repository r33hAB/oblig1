package com.example.quizapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quizapp.viewmodel.QuizPhase
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test 2: Quiz-score test.
 *
 * Verifiserer at scoren oppdateres korrekt etter riktig og feil svar.
 * QuizActivity bruker Jetpack Compose, så vi bruker createAndroidComposeRule.
 * ViewModel er tilgjengelig via activity for å lese riktig svar og verifisere score.
 */
@RunWith(AndroidJUnit4::class)
class QuizTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<QuizActivity>()

    /**
     * Tester at quiz-scoren oppdateres korrekt etter ett feil og ett riktig svar.
     *
     * Trinn:
     * 1. QuizActivity starter og laster et tilfeldig spørsmål med 3 svaralternativer.
     * 2. Vi leser riktig svar fra ViewModel og velger et FEIL alternativ.
     * 3. Verifiser at "Feil!" vises, scoreRiktige=0 og scoreTotalt=1.
     * 4. Klikk "Neste spørsmål" for å få nytt spørsmål.
     * 5. Vi leser riktig svar fra ViewModel og velger det RIKTIGE alternativet.
     * 6. Verifiser at "Riktig!" vises, scoreRiktige=1 og scoreTotalt=2.
     */
    @Test
    fun quiz_score_updates_correctly_after_right_and_wrong_answer() {
        val viewModel = composeTestRule.activity.viewModel

        // Vent til første spørsmål er lastet
        composeTestRule.waitUntil(5000) {
            viewModel.uiState.value.phase is QuizPhase.VenterPaaSvar
        }

        // --- Svar FEIL ---
        val phase1 = viewModel.uiState.value.phase as QuizPhase.VenterPaaSvar
        val correctAnswer1 = phase1.gjeldendeBilder.navn
        val wrongAnswer1 = phase1.svaralternativer.first { it != correctAnswer1 }

        composeTestRule.onNodeWithText(wrongAnswer1).performClick()

        // Verifiser at "Feil!" vises
        composeTestRule.onNodeWithText("Feil!").assertExists()

        // Verifiser score: 0 riktige, 1 totalt
        assertEquals("scoreRiktige skal være 0 etter feil svar", 0, viewModel.uiState.value.scoreRiktige)
        assertEquals("scoreTotalt skal være 1 etter første svar", 1, viewModel.uiState.value.scoreTotalt)

        // --- Gå til neste spørsmål ---
        composeTestRule.onNodeWithText("Neste sp\u00F8rsm\u00E5l").performClick()

        // Vent til neste spørsmål er lastet
        composeTestRule.waitUntil(5000) {
            viewModel.uiState.value.phase is QuizPhase.VenterPaaSvar
        }

        // --- Svar RIKTIG ---
        val phase2 = viewModel.uiState.value.phase as QuizPhase.VenterPaaSvar
        val correctAnswer2 = phase2.gjeldendeBilder.navn

        composeTestRule.onNodeWithText(correctAnswer2).performClick()

        // Verifiser at "Riktig!" vises
        composeTestRule.onNodeWithText("Riktig!").assertExists()

        // Verifiser score: 1 riktig, 2 totalt
        assertEquals("scoreRiktige skal være 1 etter riktig svar", 1, viewModel.uiState.value.scoreRiktige)
        assertEquals("scoreTotalt skal være 2 etter to svar", 2, viewModel.uiState.value.scoreTotalt)
    }
}
