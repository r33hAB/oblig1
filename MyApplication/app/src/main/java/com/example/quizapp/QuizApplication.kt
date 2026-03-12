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
import androidx.room.Room
import com.example.quizapp.data.BildeOppforing
import com.example.quizapp.data.GalleryRepository
import com.example.quizapp.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class QuizApplication : Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "gallery.db"
        ).build()
    }

    val galleryRepository: GalleryRepository by lazy {
        GalleryRepository(database.galleryItemDao())
    }

    val bildeOppforinger: MutableList<BildeOppforing> = mutableListOf()

    private var nesteId: Long = 1000

    // CoroutineScope for arbeid som skal kjøre i bakgrunnen når appen starter
    // Vi bruker IO-dispatcher fordi databaseoperasjoner ikke skal kjøres på hovedtråden
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // Når appen starter, sjekker vi om databasen allerede inneholder bilder.
        // Hvis tom, legger vi inn de innebygde startbildene én gang.
        applicationScope.launch {
            if (database.galleryItemDao().getAllSync().isEmpty()) {
                lastInnebygdeBilder()
            }
        }
    }

    private suspend fun lastInnebygdeBilder() {
        // Liste over innebygde startbilder som skal finnes i appen fra første oppstart
        val innebygdeBilder = listOf(
            "Katt" to R.drawable.dyr1,
            "Hund" to R.drawable.dyr2,
            "Kanin" to R.drawable.dyr3
        )

        // Går gjennom hvert innebygd bilde, lager en resource-URI, og lagrer metadataen i Room-databasen
        innebygdeBilder.forEach { (navn, ressursId) ->
            val uri = Uri.parse( "android.resource://${packageName}/${ressursId}" )

            // Opprett oppføringen med erInnebygd = true
            galleryRepository.insert(
                BildeOppforing(
                    id = 0L,            // 0L brukes fordi Room genererer id automatisk
                    navn = navn,        // Navnet som vises i galleri og brukes i quiz
                    bildeUri = uri,     // URI til det innebygde bildet
                    erInnebygd = true   // Market at dette er et forhåndslastet bilde
                )
            )
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
