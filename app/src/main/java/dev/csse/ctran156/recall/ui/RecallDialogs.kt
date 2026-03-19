package dev.csse.ctran156.recall.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss, title = {
        Text("Delete Bookmarks?")
    }, text = {
        Text("This action cannot be undone.")
    }, confirmButton = {
        TextButton(onClick = onConfirm) {
            Text("Delete")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}

@Composable
fun AddBookmarkDialog(
    modifier: Modifier = Modifier,
    viewModel: RecallViewModel = viewModel<RecallViewModel>(factory = RecallViewModel.Factory),
    onDismiss: () -> Unit,
    initialName: String? = "",
    initialUri: String? = ""
) {
    var name by rememberSaveable { mutableStateOf(initialName?.removePrefix("Sharing ") ?: "") }
    var uri by rememberSaveable { mutableStateOf(initialUri  ?: "") }
    var description by rememberSaveable { mutableStateOf("") }
    val tags = rememberSaveable { mutableStateListOf<String>() }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        val scrollState = rememberScrollState()

        Card(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                BookmarkTextInput(
                    text = name,
                    label = "Enter bookmark name",
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )

                BookmarkTextInput(
                    text = uri,
                    label = "Enter bookmark uri",
                    onValueChange = { uri = it },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )

                BookmarkTagsInput(
                    tagList = tags,
                    availableTags = viewModel.tagList,
                    onTagAdded = { tags.add(it) },
                    onTagRemoved = { tags.remove(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                BookmarkTextInput(
                    text = description,
                    label = "Enter detailed description",
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5 // allow multiple lines
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ), onClick = { onDismiss() }) {
                        Text("Cancel")
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ), onClick = {
                            viewModel.addBookmark(
                                name = name, description = description, uri = uri, tags = tags
                            )
                            onDismiss()
                        }) {
                        Text("Add Bookmark")
                    }
                }
            }
        }
    }
}

@Composable
fun BookmarkTextInput(
    text: String,
    modifier: Modifier = Modifier,
    label: String = "Enter text",
    onValueChange: (String) -> Unit,
    maxLines: Int = Int.MAX_VALUE
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        value = text,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        maxLines = maxLines
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookmarkTagsInput(
    tagList: List<String>,
    availableTags: List<String>,
    onTagAdded: (String) -> Unit,
    onTagRemoved: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var newTag by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier) {

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableTags.forEach { tag ->

                Row(
                    modifier = Modifier
                        .background(Color(0x18000000))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = tag in tagList, onCheckedChange = { checked ->
                            if (checked) onTagAdded(tag)
                            else onTagRemoved(tag)
                        })

                    Text(tag)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                value = newTag,
                onValueChange = { newTag = it },
                label = { Text("Add tag") },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    val tag = newTag.trim()
                    if (tag.isNotEmpty()) {
                        onTagAdded(tag)
                        newTag = ""
                    }
                }) {
                Text("Add")
            }
        }
    }
}

//@OptIn(ExperimentalLayoutApi::class)
//@Composable
//fun BookmarkTagsInput(
//    tagList: List<String>,
//    onTagAdded: (String) -> Unit,
//    onTagRemoved: (String) -> Unit,
//    modifier: Modifier = Modifier,
//    viewModel: RecallViewModel = viewModel<RecallViewModel>()
//) {
//    FlowRow(
//        modifier = modifier, horizontalArrangement = Arrangement.Center
//    ) {
//        viewModel.tagList.forEach { tag ->
//            Row(
//                modifier = Modifier
//                    .padding(8.dp)
//                    .background(Color(0x18000000)),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Checkbox(
//                    modifier = Modifier.padding(0.dp),
//                    checked = tagList.contains(tag),
//                    onCheckedChange = { checked ->
//                        if (checked) onTagAdded(tag)
//                        else onTagRemoved(tag)
//                    })
//                Text(
//                    tag,
//                    style = MaterialTheme.typography.labelMedium,
//                    modifier = Modifier.padding(end = 16.dp)
//                )
//            }
//        }
//    }
//}
