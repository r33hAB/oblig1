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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

class GalleriActivity : ComponentActivity() {

    private lateinit var quizApp: QuizApplication
    private var bildeOppforinger by mutableStateOf<List<BildeOppforing>>(emptyList())

    // TODO: Opprett states for dialoger og ventende URI
    // ventendeUri, visLeggTilDialog, visSlettDialog, oppforingTilSletting

    // TODO: Opprett ActivityResultLauncher for å velge bilder
    // registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri -> ... }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizApp = applicationContext as QuizApplication
        oppdaterBildeliste()

        setContent {
            QuizAppTheme {
                GalleriSkjerm()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        oppdaterBildeliste()
    }

    private fun oppdaterBildeliste() {
        bildeOppforinger = quizApp.bildeOppforinger.toList()
    }

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
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GalleriSkjerm() {
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
                    },
                    actions = {
                        // TODO: Sorteringsmeny kommer i neste steg
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(
                                Icons.Filled.MoreVert,
                                contentDescription = "Mer"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /* TODO: legg til bilde*/ }) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(R.string.legg_til)
                    )
                }
            }
        ) { paddingVerdier ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingVerdier)
                    .padding(16.dp)
            ) {
                if (bildeOppforinger.isEmpty()) {
                    TomtGalleriMelding()
                } else {
                    BildeGalleri(bildeOppforinger)
                }
            }
        }
    }

    @Composable
    fun TomtGalleriMelding() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Galleriet er tomt. \nTrykk for å legge til et bilde.",
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun BildeGalleri(oppforinger: List<BildeOppforing>) {
        val context = LocalContext.current

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(oppforinger) { opp ->
                BildeKort(
                    oppforing = opp,
                    onSlett = {/* TODO: slett kommer senere */ }
                )
            }
        }
    }

    // TODO Implementer BildeKort(oppforing: BildeOppforing, onSlett: () -> Unit) Composable
    // Card med bilde, navn og slett-knapp
    @Composable
    fun BildeKort(
        oppforing: BildeOppforing,
        onSlett: () -> Unit
    ) {
        val context = LocalContext.current

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(oppforing.bildeUri)
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = oppforing.navn,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = oppforing.navn,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onSlett) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Slett"
                    )
                }
            }
        }
    }


    // TODO: Implementer LeggTilNavnDialog(onBekreft: (String) -> Unit, onAvbryt: () -> Unit) Composable
    // AlertDialog med TextField for navn

    // TODO: Implementer BekreftSlettingDialog(...) Composable
    // AlertDialog med bekreftelse før sletting
}
