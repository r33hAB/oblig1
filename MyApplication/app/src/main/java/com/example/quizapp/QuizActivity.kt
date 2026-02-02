/**
 * QuizActivity.kt - Quiz-spillet med Jetpack Compose
 *
 * TODO: Implementer denne aktiviteten!
 *
 * Denne aktiviteten skal:
 * - Vise et tilfeldig bilde fra galleriet
 * - Vise 3 svaralternativer (1 riktig + 2 feil)
 * - Gi tilbakemelding på om svaret var riktig/feil
 * - Holde styr på score (riktige/totalt/prosent)
 * - Håndtere edge case når det er for få bilder
 *
 * TIPS:
 * - Se solutions/QuizActivity.kt for fullstendig løsning
 * - Bruk sealed class for å representere quiz-tilstander
 * - Bruk when() for å vise riktig UI basert på tilstand
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

/**
 * TODO: Implementer sealed class for quiz-tilstander
 *
 * Eksempel:
 * sealed class QuizTilstand {
 *     data class VenterPaaSvar(val gjeldendeBilde: BildeOppforing, val svaralternativer: List<String>) : QuizTilstand()
 *     data class SvarGitt(val gjeldendeBilde: BildeOppforing, val valgtSvar: String, val riktigSvar: String, val erRiktig: Boolean) : QuizTilstand()
 *     data object ForFaaBilder : QuizTilstand()
 * }
 */

class QuizActivity : ComponentActivity() {

    private lateinit var quizApp: QuizApplication

    // TODO: Opprett state for quiz-tilstand
    //private var tilstand by mutableStateOf<QuizTilstand>(QuizTilstand.ForFaaBilder)

    // TODO: Opprett states for score
    //antallRiktige, antallTotalt

    companion object {
        const val MINIMUM_BILDER = 3
        const val ANTALL_FEIL_SVAR = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        quizApp = applicationContext as QuizApplication

        // TODO: Last første spørsmål

        setContent {
            QuizAppTheme {
                // TODO: Erstatt dette med QuizSkjerm()
                PlaceholderSkjerm()
            }
        }
    }

    // TODO: Implementer lastNyttSporsmal()
    // 1. Sjekk om det er nok bilder (minimum 3)
    // 2. Velg tilfeldig bilde
    // 3. Hent feil svar
    // 4. Bland alle svar
    // 5. Oppdater tilstand

    // TODO: Implementer behandleSvar(valgtSvar: String)
    // 1. Sjekk om svaret er riktig
    // 2. Oppdater statistikk
    // 3. Oppdater tilstand til SvarGitt

    /**
     * Placeholder - erstatt med faktisk implementasjon
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PlaceholderSkjerm() {
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingVerdier),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TODO: Implementer quiz-spillet",
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // TODO: Implementer QuizSkjerm() Composable
    // Scaffold med TopAppBar
    // ScoreVisning øverst
    // when(tilstand) for å vise riktig innhold:
    //  ForFaaBilder -> ForFaaBilderMelding()
    //   VenterPaaSvar -> SporsmalVisning()
    //   SvarGitt -> ResultatVisning()

    // TODO: Implementer ScoreVisning() Composable
    // Card med riktige, totalt, og prosent

    // TODO: Implementer ForFaaBilderMelding() Composable
    // Melding om at det trengs minst 3 bilder

    // TODO: Implementer SporsmalVisning(tilstand: QuizTilstand.VenterPaaSvar) Composable
    //Vis bilde
    //Vis 3 svarknapper

    // TODO: Implementer ResultatVisning(tilstand: QuizTilstand.SvarGitt) Composable
    //Vis om svaret var riktig/feil
    //Vis riktig svar hvis feil
    //Vis "Neste"-knapp
}
