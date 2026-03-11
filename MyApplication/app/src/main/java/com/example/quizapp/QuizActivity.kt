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
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.quizapp.ui.theme.QuizAppTheme
import com.example.quizapp.viewmodel.QuizPhase
import com.example.quizapp.viewmodel.QuizViewModel
import com.example.quizapp.viewmodel.QuizViewModelFactory


class QuizActivity : ComponentActivity() {

    private val viewModel: QuizViewModel by viewModels {
        val app = application as QuizApplication
        QuizViewModelFactory(app.galleryRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuizAppTheme {
                QuizSkjerm()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun QuizSkjerm() {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                ScoreVisning(
                    scoreRiktige = uiState.scoreRiktige,
                    scoreTotalt = uiState.scoreTotalt
                )
                Spacer(modifier = Modifier.height(16.dp))
                when (val phase = uiState.phase) {
                    is QuizPhase.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is QuizPhase.ForFaaBilder -> ForFaaBilderMelding()
                    is QuizPhase.VenterPaaSvar -> SporsmalVisning(phase)
                    is QuizPhase.SvarGitt -> ResultatVisning(phase)
                }
            }
        }
    }

    @Composable
    fun ScoreVisning(scoreRiktige: Int, scoreTotalt: Int) {
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
                        text = "$scoreRiktige",
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
                        text = "$scoreTotalt",
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
                        text = if (scoreTotalt > 0) {
                            "${(scoreRiktige * 100 / scoreTotalt)}%"
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
    fun SporsmalVisning(phase: QuizPhase.VenterPaaSvar) {
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.quiz_sporsmal),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(phase.gjeldendeBilder.bildeUri).crossfade(true).build()
                ),
                contentDescription = stringResource(R.string.quiz_bilde_beskrivelse),
                modifier = Modifier.size(250.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(32.dp))

            phase.svaralternativer.forEach { svar ->
                Button(
                    onClick = { viewModel.submitAnswer(svar) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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

    @Composable
    fun ResultatVisning(phase: QuizPhase.SvarGitt) {
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (phase.erRiktig) {
                    stringResource(R.string.svar_riktig)
                } else {
                    stringResource(R.string.svar_feil)
                },
                style = MaterialTheme.typography.headlineMedium,
                color = if (phase.erRiktig) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(phase.gjeldendeBilder.bildeUri).crossfade(true).build()
                ),
                contentDescription = phase.riktigSvar,
                modifier = Modifier.size(200.dp).clip(RoundedCornerShape(16.dp)).background(
                    MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!phase.erRiktig) {
                Text(
                    text = stringResource(R.string.riktig_svar_var, phase.riktigSvar),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = stringResource(R.string.du_svarte, phase.valgtSvar),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.nextQuestion() },
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