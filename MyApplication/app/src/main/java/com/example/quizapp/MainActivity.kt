/**
 * MainActivity.kt - Hovedmenyen for Quiz-appen
 *
 * Denne aktiviteten viser hovedmenyen med to knapper:
 * - "Galleri" - åpner GalleriActivity for å se og administrere bilder
 * - "Quiz" - åpner QuizActivity for å spille quiz-spillet
 *
 * DESIGNVALG:
 * - Oppgaven krever at hovedmenyen bruker XML-layout (ikke Compose)
 * - Vi bruker ConstraintLayout for fleksibel posisjonering
 * - Intent brukes for å navigere mellom aktiviteter
 * - Edge-to-edge design for moderne utseende
 *
 * NAVIGASJON:
 * - MainActivity er launcher-aktiviteten (startpunktet)
 * - Den starter andre aktiviteter med startActivity(Intent)
 * - Tilbake-navigasjon håndteres automatisk av Android
 */

package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Hovedaktiviteten som viser hovedmenyen.
 *
 * AppCompatActivity er basisklassen som gir bakoverkompatibilitet
 * for moderne Android-funksjoner på eldre enheter.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Kalles når aktiviteten opprettes.
     *
     * Her setter vi opp:
     * 1. Edge-to-edge visning for moderne utseende
     * 2. XML-layouten fra activity_main.xml
     * 3. Håndtering av system-innstikk (status bar, navigasjonsbar)
     * 4. Klikklyttere for navigasjonsknappene
     *
     * @param savedInstanceState Lagret tilstand fra tidligere instans (hvis noen)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aktiverer edge-to-edge for å tegne under system-barer
        enableEdgeToEdge()

        // Setter XML-layouten som innholdet for denne aktiviteten
        // R.layout.activity_main peker til res/layout/activity_main.xml
        setContentView(R.layout.activity_main)

        // Håndterer system-innstikk (padding for statusbar og navigasjonsbar)
        // Dette sikrer at innholdet ikke overlapper med system-UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setter opp klikklyttere for navigasjonsknappene
        settOppKnapper()
    }

    /**
     * Setter opp klikklyttere for hovedmenyens knapper.
     *
     * findViewById brukes for å finne views definert i XML-layouten.
     * Returtypen er generisk, så vi spesifiserer Button for typesikkerhet.
     */
    private fun settOppKnapper() {
        // Finner galleri-knappen fra XML-layouten
        val galleriKnapp = findViewById<Button>(R.id.knappGalleri)

        // Finner quiz-knappen fra XML-layouten
        val quizKnapp = findViewById<Button>(R.id.knappQuiz)

        // Setter klikklytter for galleri-knappe
        galleriKnapp.setOnClickListener {
            apneGalleri()
        }

        // Setter klikklytter for quiz-knappen
        quizKnapp.setOnClickListener {
            apneQuiz()
        }
    }

    /**
     * Åpner galleri-aktiviteten.
     *
     * Intent er en melding som brukes for kommunikasjon mellom komponenter.
     * Her bruker vi en eksplisitt Intent som spesifiserer nøyaktig
     * hvilken aktivitet vi vil starte.
     *
     * Syntaks: Intent(context, MålKlasse::class.java)
     * - this refererer til denne aktiviteten (kontekst)
     * - GalleriActivity::class.java er Kotlin-måten å få Java Class-objektet
     */
    private fun apneGalleri() {
        // Opprett Intent for å starte GalleriActivity
        val intent = Intent(this, GalleriActivity::class.java)

        // Start aktiviteten - Android vil vise den over denne
        startActivity(intent)
    }

    /**
     * Åpner quiz-aktiviteten.
     *
     * Fungerer på samme måte som apneGalleri(), men starter
     * QuizActivity istedenfor.
     */
    private fun apneQuiz() {
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }
}
