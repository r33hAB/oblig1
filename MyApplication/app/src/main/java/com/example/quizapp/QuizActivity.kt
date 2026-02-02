/**
 * QuizActivity.kt - Quiz-spillet med Jetpack Compose
 *
 * Denne aktiviteten implementerer quiz-spillet hvor brukeren skal
 * gjette hvilket navn som hører til et tilfeldig valgt bilde.
 *
 * SPILLFLYT:
 * 1. Et tilfeldig bilde velges fra galleriet
 * 2. Tre svaralternativer vises (1 riktig + 2 feil)
 * 3. Brukeren velger et svar
 * 4. Tilbakemelding vises (riktig/feil + evt. riktig svar)
 * 5. Brukeren klikker "Neste" for nytt spørsmål
 * 6. Score oppdateres kontinuerlig
 *
 * DESIGNVALG:
 * - State machine pattern: QuizTilstand representerer de ulike tilstandene
 * - shuffled() brukes for tilfeldig rekkefølge på svaralternativer
 * - Animerte overganger for bedre brukeropplevelse
 *
 * EDGE CASES:
 * - Hvis galleriet har færre enn 3 bilder, vises en feilmelding
 * - Quiz krever minst 3 bilder for å ha meningsfulle feil-svar
 */

package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.quizapp.data.BildeOppforing
import com.example.quizapp.ui.theme.QuizAppTheme

sealed class QuizTilstand {
    data class VenterPaaSvar(
        val gjeldendeBilde: BildeOppforing,
        val svaralternativer: List<String>
    ) : QuizTilstand()

    data class SvarGitt(
        val gjeldendeBilde: BildeOppforing,
        val valgtSvar: String,
        val riktigSvar: String,
        val erRiktig: Boolean
    ) : QuizTilstand()

    data object ForFaaBilder : QuizTilstand()
}


class QuizActivity : ComponentActivity() {


    private lateinit var quizApp: QuizApplication

    private var tilstand by mutableStateOf<QuizTilstand>(QuizTilstand.ForFaaBilder)

    private var antallRiktige by mutableStateOf(0)

    /**
     * Totalt antall spørsmål besvart i denne sesjonen.
     */
    private var antallTotalt by mutableStateOf(0)

    /**
     * Minimum antall bilder som kreves for å spille quiz.
     * Vi trenger minst 3 for å ha 1 riktig og 2 feil svar.
     */
    companion object {
        const val MINIMUM_BILDER = 3
        const val ANTALL_FEIL_SVAR = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hent referanse til Application-klassen
        quizApp = applicationContext as QuizApplication

        // Start quizen med første spørsmål
        lastNyttSporsmal()

        // Sett Compose-innhold
        setContent {
            QuizAppTheme {
                QuizSkjerm()
            }
        }
    }

    private fun lastNyttSporsmal() {
        // Sjekk at vi har nok bilder
        if (quizApp.bildeOppforinger.size < MINIMUM_BILDER) {
            tilstand = QuizTilstand.ForFaaBilder
            return
        }

        // Velg tilfeldig bilde
        val riktigBilde = quizApp.hentTilfeldigOppforing()!!

        // Hent feil svar (navn fra andre bilder)
        val feilSvar = quizApp.hentFeilSvar(riktigBilde.id, ANTALL_FEIL_SVAR)

        // Kombiner riktig og feil svar, og bland dem
        val alleSvar = (listOf(riktigBilde.navn) + feilSvar).shuffled()

        // Oppdater tilstand til å vente på svar
        tilstand = QuizTilstand.VenterPaaSvar(
            gjeldendeBilde = riktigBilde,
            svaralternativer = alleSvar
        )
    }

    /**
     * Håndterer når brukeren velger et svar.
     *
     * @param valgtSvar Svaret brukeren klikket på
     */
    private fun behandleSvar(valgtSvar: String) {
        val gjeldendeTilstand = tilstand as? QuizTilstand.VenterPaaSvar ?: return

        val riktigSvar = gjeldendeTilstand.gjeldendeBilde.navn
        val erRiktig = valgtSvar == riktigSvar

        // Oppdater statistikk
        antallTotalt++
        if (erRiktig) {
            antallRiktige++
        }

        // Oppdater tilstand til å vise resultat
        tilstand = QuizTilstand.SvarGitt(
            gjeldendeBilde = gjeldendeTilstand.gjeldendeBilde,
            valgtSvar = valgtSvar,
            riktigSvar = riktigSvar,
            erRiktig = erRiktig
        )
    }

    /**
     * Hovedskjermen for quizen.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun QuizSkjerm() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.quiz_tittel)) },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.tilbake)
                            )
                        }
                    }
                )
            }
        ) { paddingVerdier ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingVerdier)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Score-visning
                ScoreVisning()

                Spacer(modifier = Modifier.height(16.dp))

                // Hovedinnhold basert på tilstand
                when (val t = tilstand) {
                    is QuizTilstand.ForFaaBilder -> ForFaaBilderMelding()
                    is QuizTilstand.VenterPaaSvar -> SporsmalVisning(t)
                    is QuizTilstand.SvarGitt -> ResultatVisning(t)
                }
            }
        }
    }

    /**
     * Viser gjeldende score.
     */
    @Composable
    fun ScoreVisning() {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.score_riktige),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "$antallRiktige",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.score_totalt),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "$antallTotalt",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.score_prosent),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = if (antallTotalt > 0) {
                            "${(antallRiktige * 100 / antallTotalt)}%"
                        } else {
                            "-%"
                        },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    /**
     * Melding når det er for få bilder til å spille quiz.
     */
    @Composable
    fun ForFaaBilderMelding() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.for_faa_bilder_tittel),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.for_faa_bilder_melding),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { finish() }) {
                    Text(stringResource(R.string.gaa_til_galleri))
                }
            }
        }
    }

    @Composable
    fun SporsmalVisning(tilstand: QuizTilstand.VenterPaaSvar) {
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Spørsmålstekst
            Text(
                text = stringResource(R.string.quiz_sporsmal),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bilde
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(tilstand.gjeldendeBilde.bildeUri)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = stringResource(R.string.quiz_bilde_beskrivelse),
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Svaralternativer
            tilstand.svaralternativer.forEach { svar ->
                Button(
                    onClick = { behandleSvar(svar) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = svar,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }

    /**
     * Viser resultatet etter at brukeren har svart.
     */
    @Composable
    fun ResultatVisning(tilstand: QuizTilstand.SvarGitt) {
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Resultat-tekst
            Text(
                text = if (tilstand.erRiktig) {
                    stringResource(R.string.svar_riktig)
                } else {
                    stringResource(R.string.svar_feil)
                },
                style = MaterialTheme.typography.headlineMedium,
                color = if (tilstand.erRiktig) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bilde
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(tilstand.gjeldendeBilde.bildeUri)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = tilstand.riktigSvar,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Vis riktig svar hvis feil
            if (!tilstand.erRiktig) {
                Text(
                    text = stringResource(R.string.riktig_svar_var, tilstand.riktigSvar),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Vis hva brukeren svarte
            Text(
                text = stringResource(R.string.du_svarte, tilstand.valgtSvar),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Neste-knapp
            Button(
                onClick = { lastNyttSporsmal() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.neste_sporsmal),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
