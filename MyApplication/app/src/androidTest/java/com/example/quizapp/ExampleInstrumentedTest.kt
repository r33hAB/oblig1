/**
 * ExampleInstrumentedTest.kt - Instrumenterte tester for Quiz-appen
 *
 * Instrumenterte tester kjører på en faktisk Android-enhet eller emulator.
 * De har tilgang til Android-rammeverket og kan teste brukergrensesnittet,
 * aktiviteter, og interaksjoner som krever en Android-kontekst.
 *
 * Disse testene er tregere enn enhetstester, men kan teste mer kompleks
 * funksjonalitet som involverer Android-komponenter.
 */

package com.example.quizapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * Instrumentert test som kjører på en Android-enhet.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    /**
     * Verifiserer at appen har riktig pakkenavn.
     *
     * InstrumentationRegistry gir tilgang til test-kontekst som lar
     * oss hente informasjon om appen under test.
     */
    @Test
    fun useAppContext() {
        // Hent konteksten til appen under test
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // Verifiser at pakkenavnet er korrekt
        assertEquals("com.example.quizapp", appContext.packageName)
    }
}
