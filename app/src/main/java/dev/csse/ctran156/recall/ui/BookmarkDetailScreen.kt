package dev.csse.ctran156.recall.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.net.toUri
import dev.csse.ctran156.recall.data.BookmarkEntity
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.LineBreak
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun BookmarkDetailScreen(
    bookmark: BookmarkEntity,
    modifier: Modifier = Modifier,
    viewModel: RecallViewModel = viewModel<RecallViewModel>(factory = RecallViewModel.Factory)
) {
    val uiState = viewModel.uiState.collectAsState().value
    val uris = uiState.uris
    val tags = uiState.tags
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    BoxWithConstraints(modifier = modifier) {
        if (maxWidth.value < 1.5 * maxHeight.value) {
            Column(modifier = Modifier.padding(16.dp)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = bookmark.name,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        bookmark.description?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    lineBreak = LineBreak.Paragraph
                                ),
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            tags.forEach { tag ->
                                Text(
                                    tag.tag, modifier = Modifier
                                        .alignByBaseline()
                                        .background(
                                            MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(4.dp), color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
                LazyColumn(modifier = modifier.weight(1f)) {
                    stickyHeader {
                        if (uris.isNotEmpty()) {
                            Text(
                                "URIS",
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.background)
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.labelLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    items(items = uris) { uri ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(
                                formatter.format(uri.date),
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 8.dp)
                            )
                            val context = LocalContext.current
                            Text(
                                uri.uri,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 8.dp)
                                    .clickable {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW, uri.uri.toUri()
                                        )
                                        context.startActivity(intent)
                                    })
                        }
                    }
                }
                val keyboardController = LocalSoftwareKeyboardController.current
                var uri by rememberSaveable { mutableStateOf("") }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    value = uri,
                    onValueChange = { uri = it },
                    label = { Text("Add a uri…") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        viewModel.addUri(
                            uri = uri, id = bookmark.id
                        )
                        uri = ""
                    })
                )
            }

        } else {
            Row(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                // Left side: Bookmark info
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = bookmark.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        bookmark.description?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    lineBreak = LineBreak.Paragraph
                                ),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            tags.forEach { tag ->
                                Text(
                                    tag.tag,
                                    modifier = Modifier
                                        .alignByBaseline()
                                        .background(
                                            MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(4.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }

                // Right side: URIs and input
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 16.dp)
                ) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        stickyHeader {
                            if (uris.isNotEmpty()) {
                                Text(
                                    "URIS",
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.background)
                                        .fillMaxWidth(),
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        items(items = uris) { uri ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Text(
                                    formatter.format(uri.date),
                                    textAlign = TextAlign.End,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp, 8.dp)
                                )
                                val context = LocalContext.current
                                Text(
                                    uri.uri,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp, 8.dp)
                                        .clickable {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW, uri.uri.toUri()
                                            )
                                            context.startActivity(intent)
                                        })
                            }
                        }
                    }

                    // URI input at bottom
                    val keyboardController = LocalSoftwareKeyboardController.current
                    var uri by rememberSaveable { mutableStateOf("") }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        value = uri,
                        onValueChange = { uri = it },
                        label = { Text("Add a uri…") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                            viewModel.addUri(
                                uri = uri, id = bookmark.id
                            )
                            uri = ""
                        })
                    )
                }
            }
        }
    }
}