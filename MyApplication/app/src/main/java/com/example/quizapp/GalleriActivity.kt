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
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text


class GalleriActivity : ComponentActivity() {

    private lateinit var quizApp: QuizApplication
    private var bildeOppforinger by mutableStateOf<List<BildeOppforing>>(emptyList())
    private var ventendeUri by mutableStateOf<Uri?>(null)
    private var visLeggTilDialog by mutableStateOf(false)

    private var oppforingTilSletting by mutableStateOf<BildeOppforing?>(null)
    private var visSlettDialog by mutableStateOf(false)

    private var visMeny by mutableStateOf(false)

    private val velgBildeLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri ?: return@registerForActivityResult
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            ventendeUri = uri
            visLeggTilDialog = true
        }

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
                        Box {
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
                                        quizApp.sorterAlfabetisk()
                                        oppdaterBildeliste()
                                        visMeny = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.sorter_aa_a)) },
                                    onClick = {
                                        quizApp.sorterOmvendtAlfabetisk()
                                        oppdaterBildeliste()
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                if (bildeOppforinger.isEmpty()) {
                    TomtGalleriMelding()
                } else {
                    BildeGalleri(bildeOppforinger)
                }
            }
            if (visLeggTilDialog && ventendeUri != null) {
                LeggTilNavnDialog(
                    onBekreft = { navn ->
                        val uri = ventendeUri ?: return@LeggTilNavnDialog
                        quizApp.leggTilOppforing(navn, uri)
                        ventendeUri = null
                        visLeggTilDialog = false
                        oppdaterBildeliste()
                    },
                    onAvbryt = {
                        ventendeUri = null
                        visLeggTilDialog = false
                    }
                )
            }
            if (visSlettDialog && oppforingTilSletting != null) {
                BekreftSlettingDialog(
                    navn = oppforingTilSletting!!.navn,
                    onBekreft = {
                        val id = oppforingTilSletting!!.id
                        quizApp.fjernOppforing(id)
                        oppforingTilSletting = null
                        visSlettDialog = false
                        oppdaterBildeliste()
                    },
                    onAvbryt = {
                        oppforingTilSletting = null
                        visSlettDialog = false
                    }
                )
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

    @Composable
    fun LeggTilNavnDialog(onBekreft: (String) -> Unit, onAvbryt: () -> Unit) {
        var inputNavn by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onAvbryt,
            title = { Text(stringResource(R.string.legg_til_navn_tittel)) },
            text = {
                OutlinedTextField(
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
    fun BekreftSlettingDialog(navn: String, onBekreft: () -> Unit, onAvbryt: () -> Unit) {
        AlertDialog(
            onDismissRequest = onAvbryt,
            title = { Text(stringResource(R.string.bekreft_sletting_tittel)) },
            text = { Text(stringResource(R.string.bekreft_sletting_melding, navn)) },
            confirmButton = {
                TextButton(onClick = onBekreft) {
                    Text(stringResource(R.string.slett)) }
            },
            dismissButton = {
                TextButton(onClick = onAvbryt) {
                    Text(stringResource(R.string.avbryt)) }
            }
        )
    }
}


