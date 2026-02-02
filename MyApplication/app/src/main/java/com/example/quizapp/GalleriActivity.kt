/**
 * GalleriActivity.kt - Galleri-visning med Jetpack Compose
 *
 * TODO: Implementer denne aktiviteten!
 *
 * Denne aktiviteten skal:
 * - Vise alle bilder i galleriet med LazyColumn
 * - La brukeren legge til nye bilder fra telefonen
 * - La brukeren slette bilder
 * - La brukeren sortere galleriet
 *
 * - Bruk Scaffold med TopAppBar og FloatingActionButton
 * - Bruk LazyColumn for listen
 * - Bruk AlertDialog for dialoger
 */

package com.example.quizapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.quizapp.data.BildeOppforing
import com.example.quizapp.ui.theme.QuizAppTheme

class GalleriActivity : ComponentActivity() {

    private lateinit var quizApp: QuizApplication

    // TODO: Opprett mutableStateOf for bildeOppforinger
    // private var bildeOppforinger by mutableStateOf<List<BildeOppforing>>(emptyList())

    // TODO: Opprett states for dialoger og ventende URI
    // ventendeUri, visLeggTilDialog, visSlettDialog, oppforingTilSletting

    // TODO: Opprett ActivityResultLauncher for å velge bilder
    // registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri -> ... }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        quizApp = applicationContext as QuizApplication

        // TODO: Last inn bildelisten

        setContent {
            QuizAppTheme {
                // TODO: Erstatt dette med GalleriSkjerm()
                PlaceholderSkjerm()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO: Oppdater bildelisten når aktiviteten blir synlig igjen
    }

    // TODO: Implementer oppdaterBildeliste()
    // Hint: bildeOppforinger = quizApp.bildeOppforinger.toList()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PlaceholderSkjerm() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.galleri_tittel)) },
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
                    text = "TODO: Implementer galleri-visning",
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // TODO: Implementer GalleriSkjerm() Composable
    // Scaffold med TopAppBar (tittel, tilbake-knapp, sorteringsmeny)
    // FloatingActionButton for å legge til bilder
    // Vis BildeGalleri eller TomtGalleriMelding basert på om listen er tom
    // Vis dialoger for å legge til navn og bekrefte sletting

    // TODO: Implementer TomtGalleriMelding() Composable

    // TODO: Implementer BildeGalleri(oppforinger: List<BildeOppforing>) Composable
    // Bruk LazyColumn med items()

    // TODO: Implementer BildeKort(oppforing: BildeOppforing, onSlett: () -> Unit) Composable
    // Card med bilde, navn og slett-knapp

    // TODO: Implementer LeggTilNavnDialog(onBekreft: (String) -> Unit, onAvbryt: () -> Unit) Composable
    // AlertDialog med TextField for navn

    // TODO: Implementer BekreftSlettingDialog(...) Composable
    // AlertDialog med bekreftelse før sletting
}
