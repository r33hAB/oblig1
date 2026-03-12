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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.quizapp.data.BildeOppforing
import com.example.quizapp.ui.theme.QuizAppTheme
import com.example.quizapp.viewmodel.GallerySortOrder
import com.example.quizapp.viewmodel.GalleryUiState
import com.example.quizapp.viewmodel.GalleryViewModel
import com.example.quizapp.viewmodel.GalleryViewModelFactory

class GalleriActivity : ComponentActivity() {

    // ViewModel som henter data fra Room view GalleryRepository
    private val viewModel: GalleryViewModel by viewModels {
        val app = application as QuizApplication
        GalleryViewModelFactory(app.galleryRepository)
    }

    // Compose-state for "legg til bilde"-flyten: valgt URI + om navnedialog skal vises
    private var ventedeUri by mutableStateOf<Uri?>(null)
    private var visLeggTilDialog by mutableStateOf(false)

    // Compose-state for sletting: hvilken oppføring som skal slettes + om slettedialogen skal vises
    private var oppforingTilSletting by mutableStateOf<BildeOppforing?>(null)
    private var visSlettDialog by mutableStateOf(false)

    // Compose-state: om sorteringsmenyen er åpent
    private var visMeny by mutableStateOf(false)

    /**
     * Launcher for å velge bilde fra telefonen via Storage Access Framework.
     * Når brukeren velger en fil får vi en Uri.
     * - takePersistableUriPermission sørger for at appen beholder lesetilgang senere.
     * - Deretter åpner vi en dialog for å gi bildet et navn.
     */
    private val velgBildeLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        // Hvis brukeren avbryter, er uri null
        // hvis null, avslutt callbacken
        uri ?: return@registerForActivityResult
        // Ber Android om å "huske" at appen har lesetilgang til denne bilde-urien
        contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        // Lagrer den valgte Uri i state.
        ventedeUri = uri
        // Setter state slik at "gi navn"-dialogen vises
        visLeggTilDialog = true
    }

    /**
     * Starter aktiviteten:
     * - Setter Compose-innholdet (GalleriSkjerm)
     *   som nå henter data fra GalleryViewModel (Room)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuizAppTheme {
                GalleriSkjerm()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GalleriSkjerm() {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.galleri_tittel)) },
                    navigationIcon = {
                        // Tilbake: avslutter denne Activity og går tilbake til MainActivity
                        IconButton(onClick = { finish() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.tilbake)
                            )
                        }
                    },
                    actions = {
                        // Sorteringsmeny
                        Box{
                            IconButton(onClick = { visMeny = true }) {
                                Icon(
                                    Icons.Filled.MoreVert,
                                    contentDescription = stringResource(R.string.mer)
                                )
                            }
                            DropdownMenu(
                                expanded = visMeny,
                                onDismissRequest = { visMeny = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.sorter_a_aa)) },
                                    onClick = {
                                        viewModel.setSortOrder(GallerySortOrder.ASC)
                                        visMeny = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.sorter_aa_a)) },
                                    onClick = {
                                        viewModel.setSortOrder(GallerySortOrder.DESC)
                                        visMeny = false
                                    }
                                )
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { velgBildeLauncher.launch(arrayOf("image/*")) }
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(R.string.legg_til)
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)
            ) {
                when {
                    uiState.isLoading -> {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    uiState.isEmpty -> {
                        TomtGalleriMelding()
                    }

                    else -> {
                        BildeGalleri(uiState.items)
                    }
                }
            }

            if (visLeggTilDialog && ventedeUri != null) {
                LeggTilNavnDialog(
                    onBekreft = { navn ->
                        val uri = ventedeUri ?: return@LeggTilNavnDialog
                        viewModel.addImage(navn, uri.toString())
                        ventedeUri = null
                        visLeggTilDialog = false
                    },
                    onAvbryt = {
                        ventedeUri = null
                        visLeggTilDialog = false
                    }
                )
            }
            if (visSlettDialog && oppforingTilSletting != null) {
                BekreftSlettDialog(
                    navn = oppforingTilSletting!!.navn,
                    onBekreft = {
                        viewModel.deleteImage(oppforingTilSletting!!)
                        oppforingTilSletting = null
                        visSlettDialog = false
                    },
                    onAvbryt = {
                        oppforingTilSletting = null
                        visSlettDialog = false
                    }
                )
            }
        }
    }

    /**
     * Vises når galleri-listen er tom.
     */
    @Composable
    fun TomtGalleriMelding() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.tomt_galleri),
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun BildeGalleri(oppforinger: List<BildeOppforing>) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(oppforinger, key = { it.id }) { opp ->
                BildeKort(
                    oppforing = opp,
                    onSlett = {
                        oppforingTilSletting = opp
                        visSlettDialog = true
                    }
                )
            }
        }
    }

    @Composable
    fun BildeKort(oppforing: BildeOppforing, onSlett: () -> Unit) {
        val context = LocalContext.current

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context).data(oppforing.bildeUri).crossfade(true).build()
                    ),
                    contentDescription = oppforing.navn,
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)).background(
                        MaterialTheme.colorScheme.surfaceVariant),
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

    @Composable
    fun LeggTilNavnDialog(onBekreft: (String) -> Unit, onAvbryt: () -> Unit) {
        var inputNavn by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = onAvbryt,
            title = { Text(stringResource(R.string.legg_til_navn_tittel)) },
            text = {
                androidx.compose.material3.OutlinedTextField(
                    value = inputNavn,
                    onValueChange = { inputNavn = it },
                    label = { Text(stringResource(R.string.navn_hint)) },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { onBekreft(inputNavn.trim()) },
                    enabled = inputNavn.trim().isNotEmpty()
                ) { Text(stringResource(R.string.bekreft)) }
            },
            dismissButton = {
                TextButton(onClick = onAvbryt) {
                    Text(stringResource(R.string.avbryt))
                }
            }
        )
    }

    @Composable
    fun BekreftSlettDialog(navn: String, onBekreft: () -> Unit, onAvbryt: () -> Unit) {
        AlertDialog(
            onDismissRequest = onAvbryt,
            title = { Text(stringResource(R.string.bekreft_sletting_tittel)) },
            text = { Text(stringResource(R.string.bekreft_sletting_melding, navn)) },
            confirmButton = {
                TextButton(onClick = onBekreft) {
                    Text(stringResource(R.string.slett))
                }
            },
            dismissButton = {
                TextButton(onClick = onAvbryt) {
                    Text(stringResource(R.string.avbryt))
                }
            }
        )
    }
}
