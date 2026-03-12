package com.example.quizapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test 1: Navigasjonstest for hovedmenyen.
 *
 * Verifiserer at knappene i hovedmenyen navigerer til riktig aktivitet.
 * MainActivity bruker XML-layout, så vi bruker standard Espresso for view-interaksjon
 * og Espresso Intents for å verifisere at riktig Intent ble sendt.
 */
@RunWith(AndroidJUnit4::class)
class MainMenuTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    /**
     * Tester at klikk på "Galleri"-knappen starter GalleriActivity.
     *
     * Trinn:
     * 1. MainActivity vises med to knapper (Galleri og Quiz).
     * 2. Brukeren klikker på "Galleri"-knappen.
     * 3. Verifiser at en Intent med mål GalleriActivity ble sendt.
     */
    @Test
    fun clicking_gallery_button_opens_gallery_activity() {
        onView(withId(R.id.knappGalleri)).perform(click())

        Intents.intended(hasComponent(GalleriActivity::class.java.name))
    }

    /**
     * Tester at klikk på "Start Quiz"-knappen starter QuizActivity.
     *
     * Trinn:
     * 1. MainActivity vises med to knapper (Galleri og Quiz).
     * 2. Brukeren klikker på "Start Quiz"-knappen.
     * 3. Verifiser at en Intent med mål QuizActivity ble sendt.
     */
    @Test
    fun clicking_quiz_button_opens_quiz_activity() {
        onView(withId(R.id.knappQuiz)).perform(click())

        Intents.intended(hasComponent(QuizActivity::class.java.name))
    }
}
