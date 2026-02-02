/**
 * QuizApplication.kt - Application-klassen for Quiz-appen
 *
 * Application-klassen er en singleton som lever gjennom hele appens livssyklus.
 * Den opprettes før noen Activity starter og overlever konfigurasjon-endringer
 * som skjermrotasjon.
 *
 * HVORFOR APPLICATION-KLASSEN?
 * - Oppgaven krever at vi bruker Application-klassen for å dele data mellom Activities
 * - Dette er en enkel måte å ha "global" tilstand uten å bruke en database
 * - Alternativet ville vært å bruke ViewModel med SavedStateHandle, men
 *   Application-klassen er enklere for denne oppgaven
 *
 * VIKTIG BEGRENSNING:
 * - Data lagres kun i minnet (RAM)
 * - Når appen avsluttes helt (ikke bare bakgrunn), mister vi bruker-tillagte bilder
 * - Innebygde bilder lastes inn på nytt ved oppstart
 * - Persistent lagring kommer i neste oblig
 *
 * COLLECTIONS-VALG:
 * - Vi bruker MutableList<BildeOppforing> som datastruktur
 * - ArrayList er standard implementasjon og gir O(1) tilgang via indeks
 * - Sortering gjøres med sortBy/sortByDescending som er O(n log n)
 * - Dette er tilstrekkelig for et lite galleri med noen titalls bilder
 */

package com.example.quizapp

import android.app.Application
import android.net.Uri
import com.example.quizapp.data.BildeOppforing


class QuizApplication : Application() {


    val bildeOppforinger: MutableList<BildeOppforing> = mutableListOf()


    private var nesteId: Long = 1000


    override fun onCreate() {
        super.onCreate()
        lastInnebygdeBilder()
    }


    private fun lastInnebygdeBilder() {
        // Liste over innebygde bilder med navn og ressurs-ID
        val innebygdeBilder = listOf(
            Pair("Katt", R.drawable.dyr1),
            Pair("Hund", R.drawable.dyr2),
            Pair("Kanin", R.drawable.dyr3)
        )

        // Konverter hver ressurs til en BildeOppforing
        innebygdeBilder.forEachIndexed { index, (navn, ressursId) ->
            // Formatet er: android.resource://com.example.quizapp/drawable/dyr1
            val uri = Uri.parse(
                "android.resource://${packageName}/${ressursId}"
            )

            // Opprett oppføringen med erInnebygd = true
            val oppforing = BildeOppforing(
                id = (index + 1).toLong(),  // IDer 1, 2, 3 for innebygde
                navn = navn,
                bildeUri = uri,
                erInnebygd = true
            )

            bildeOppforinger.add(oppforing)
        }
    }

    fun leggTilOppforing(navn: String, bildeUri: Uri): BildeOppforing {
        val oppforing = BildeOppforing(
            id = nesteId++,
            navn = navn,
            bildeUri = bildeUri,
            erInnebygd = false
        )
        bildeOppforinger.add(oppforing)
        return oppforing
    }

    fun fjernOppforing(id: Long): Boolean {
        return bildeOppforinger.removeIf { it.id == id }
    }

    fun sorterAlfabetisk() {
        bildeOppforinger.sortBy { it.navn.lowercase() }
    }


    fun sorterOmvendtAlfabetisk() {
        bildeOppforinger.sortByDescending { it.navn.lowercase() }
    }

    fun hentTilfeldigOppforing(): BildeOppforing? {
        return bildeOppforinger.randomOrNull()
    }


    fun hentFeilSvar(riktigId: Long, antall: Int): List<String> {
        return bildeOppforinger
            .filter { it.id != riktigId }  // Ekskluder riktig svar
            .shuffled()                     // Bland tilfeldig
            .take(antall)                   // Ta ønsket antall
            .map { it.navn }               // Hent bare navnene
    }
}
