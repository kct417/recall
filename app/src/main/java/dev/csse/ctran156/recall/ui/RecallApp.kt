package dev.csse.ctran156.recall.ui

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.csse.ctran156.recall.Bookmark
import dev.csse.ctran156.recall.data.BookmarkEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URLDecoder

sealed class Routes {
    @Serializable
    data object BookmarkList

    @Serializable
    data class BookmarkDetail(val id: String)

    @Serializable
    data class AddBookmark(val sharedSubject: String?, val sharedText: String?)
}

@Composable
fun RecallApp(
    viewModel: RecallViewModel = viewModel<RecallViewModel>(factory = RecallViewModel.Factory),
    sharedSubject: String?,
    sharedText: String?
) {
    var showConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    var showBookmarkDialog by rememberSaveable { mutableStateOf(false) }
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        if (!sharedText.isNullOrBlank()) {
            showBookmarkDialog = true
//            navController.navigate(
//                Routes.AddBookmark(
//                    sharedSubject = sharedSubject,
//                    sharedText = sharedText
//                )
//            )
        }
    }

    NavHost(
        navController = navController, startDestination = Routes.BookmarkList
    ) {
        composable<Routes.BookmarkList> {
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                RecallAppBar(
                    viewModel = viewModel,
                    canDeleteBookmarks = true,
                    onDeleteAction = { showConfirmationDialog = true })
            }, bottomBar = {
                FloatingSearchBar(
                    viewModel = viewModel, onSearch = {})
            }, floatingActionButton = {
                FloatingActionButton(
                    onClick = { showBookmarkDialog = true }, modifier = Modifier.offset(y = 64.dp)
                ) {
                    Icon(
                        Icons.Default.Add, contentDescription = "Add Bookmark"
                    )
                }
            }) { innerPadding ->
                BookmarkListScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    viewModel = viewModel,
                    onSelectBookmark = { bookmark: BookmarkEntity ->
                        navController.navigate(
                            Routes.BookmarkDetail(
                                id = bookmark.id.toString()
                            )
                        )
                    })
            }
        }
        composable<Routes.BookmarkDetail> { backStackEntry ->
            val detail: Routes.BookmarkDetail = backStackEntry.toRoute()
            val bookmark: BookmarkEntity? = viewModel.findBookmarkById(detail.id)
            val context = LocalContext.current
            Scaffold(
                topBar = {
                    RecallAppBar(
                        viewModel = viewModel,
                        canNavigateBack = true,
                        canShareBookmark = true,
                        onUpClick = {
                            navController.navigate(
                                Routes.BookmarkList
                            )
                        },
                        onShareAction = {
                            if (bookmark != null) {
                                val shareLink: String = viewModel.createShareLink()

                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_SUBJECT, "Sharing ${bookmark.name}"
                                    )
                                    putExtra(
                                        Intent.EXTRA_TEXT, shareLink
                                    )
                                }

                                context.startActivity(
                                    Intent.createChooser(intent, "Recall")
                                )
                            }
                        })
                }, modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                if (bookmark != null) {
                    viewModel.selectBookmark(bookmark = bookmark)
                    BookmarkDetailScreen(
                        bookmark,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        viewModel = viewModel
                    )
                }
            }
        }
        composable<Routes.AddBookmark> { backStackEntry ->
            val detail: Routes.AddBookmark = backStackEntry.toRoute()

//            var initialName: String? = null
//            var initialUri: String? = null
//            var initialDescription: String? = null
//            var initialTags: List<String>? = null
//
//            detail.sharedText?.let { encoded ->
//                try {
//                    val json = URLDecoder.decode(encoded, "UTF-8")
//                    val sharedBookmark = Json.decodeFromString<ShareBookmark>(json)
//                    initialName = sharedBookmark.name
//                    initialUri = sharedBookmark.uri
//                    initialDescription = sharedBookmark.description
//                    initialTags = sharedBookmark.tags
//                } catch (e: Exception) {
//                    // handle error (invalid content)
//                }
//            }
            showBookmarkDialog = true


        }
    }

    if (showConfirmationDialog) {
        DeleteConfirmationDialog(onConfirm = {
            viewModel.deleteSelectedBookmarks()
            showConfirmationDialog = false
        }, onDismiss = {
            showConfirmationDialog = false
        })
    }
    if (showBookmarkDialog) {
        AddBookmarkDialog(
            modifier = Modifier.fillMaxSize(0.9f),
            viewModel = viewModel,
            onDismiss = { showBookmarkDialog = false },
            initialName = sharedSubject,
            initialUri = sharedText
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingSearchBar(
    viewModel: RecallViewModel, modifier: Modifier = Modifier, onSearch: (Bookmark) -> Unit = {}
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val queryState = rememberTextFieldState()

    SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        inputField = {
            SearchBarDefaults.InputField(
                query = queryState.text.toString(),
                onQueryChange = {
                    queryState.edit { replace(0, length, it) }
                    viewModel.updateSearchQuery(it)
                },
                onSearch = {
//                    viewModel.findBookmarkByName(name = queryState.text.toString())
                },
                expanded = expanded,
                onExpandedChange = {},
                placeholder = { Text("Search bookmarks") })
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {}
}