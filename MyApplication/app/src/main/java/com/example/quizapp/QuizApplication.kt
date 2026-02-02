/**
 * QuizApplication.kt - Application-klassen for Quiz-appen
 *
 * TODO: Implementer denne klassen!
 *
 * Denne klassen skal:
 * - Holde en liste over alle bilde-oppføringer (MutableList<BildeOppforing>)
 * - Laste inn innebygde bilder ved oppstart (onCreate)
 * - Gi metoder for å legge til, fjerne og sortere oppføringer
 * - Gi metoder for quiz-funksjonalitet (hent tilfeldig, hent feil svar)
 *
 * - Bruk mutableListOf() for å lage listen
 * - Uri.parse() for å bygge URIer til ressurser
 */

package com.example.quizapp

import android.app.Application
import android.net.Uri
import com.example.quizapp.data.BildeOppforing

class QuizApplication : Application() {

    // TODO: Opprett en MutableList for å holde alle bilde-oppføringer
    val bildeOppforinger: MutableList<BildeOppforing> = mutableListOf()

    // TODO: Opprett en teller for å generere unike IDer (start på 1000)
    private var nesteId: Long = 1000

    override fun onCreate() {
        super.onCreate()
        // TODO: Last inn innebygde bilder her
        //  Bruk R.drawable.dyr1, dyr2, dyr3
        // URI-format: "android.resource://${packageName}/${ressursId}"
    }

    /**
     * TODO: Implementer denne metoden
     * Skal legge til en ny bilde-oppføring i galleriet
     */
    fun leggTilOppforing(navn: String, bildeUri: Uri): BildeOppforing {
        // TODO: Opprett en ny BildeOppforing med unik ID
        // TODO: Legg den til i listen
        // TODO: Returner oppføringen
        TODO("Implementer leggTilOppforing")
    }

    /**
     * TODO: Implementer denne metoden
     * Skal fjerne en oppføring basert på ID
     */
    fun fjernOppforing(id: Long): Boolean {
        // TODO: Fjern oppføringen med gitt ID fra listen
        // Bruk removeIf { }
        TODO("Implementer fjernOppforing")
    }

    /**
     * TODO: Implementer denne metoden
     * Skal sortere galleriet alfabetisk (A-Å)
     */
    fun sorterAlfabetisk() {
        // TODO: Sorter listen alfabetisk på navn
        // Bruk sortBy { }
        TODO("Implementer sorterAlfabetisk")
    }

    /**
     * TODO: Implementer denne metoden
     * Skal sortere galleriet i omvendt alfabetisk rekkefølge (Å-A)
     */
    fun sorterOmvendtAlfabetisk() {
        // TODO: Sorter listen i omvendt alfabetisk rekkefølge
        // Bruk sortByDescending { }
        TODO("Implementer sorterOmvendtAlfabetisk")
    }

    /**
     * TODO: Implementer denne metoden
     * Skal returnere en tilfeldig oppføring for quiz
     */
    fun hentTilfeldigOppforing(): BildeOppforing? {
        // TODO: Returner en tilfeldig oppføring fra listen
        //Bruk randomOrNull()
        TODO("Implementer hentTilfeldigOppforing")
    }

    /**
     * TODO: Implementer denne metoden
     * Skal returnere feil svar for quiz (navn fra andre bilder)
     */
    fun hentFeilSvar(riktigId: Long, antall: Int): List<String> {
        // TODO: Returner 'antall' tilfeldige navn som IKKE er det riktige svaret

        TODO("Implementer hentFeilSvar")
    }
}
