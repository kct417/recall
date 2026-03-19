package dev.csse.ctran156.recall.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecallAppBar(
    viewModel: RecallViewModel = viewModel<RecallViewModel>(),
    canDeleteBookmarks: Boolean = false,
    canNavigateBack: Boolean = false,
    canShareBookmark: Boolean = false,
    onDeleteAction: () -> Unit = {},
    onShareAction: () -> Unit = {},
    onUpClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
        Text(
            "Recall", style = MaterialTheme.typography.displayLarge
        )
    }, colors = TopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        scrolledContainerColor = MaterialTheme.colorScheme.primary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
    ), navigationIcon = {
        if (canNavigateBack) IconButton(
            onClick = onUpClick
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Navigate back"
            )
        }
    }, actions = {
        if (canDeleteBookmarks) {
            val selectedBookmarks by viewModel.selectedBookmarks.collectAsStateWithLifecycle()
            val selectedBookmarksExist = selectedBookmarks.isNotEmpty()
            IconButton(
                enabled = selectedBookmarksExist, onClick = onDeleteAction
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete selected bookmark(s)"
                )
            }
        }
        if (canShareBookmark) {
            IconButton(
                onClick = onShareAction
            ) {
                Icon(
                    imageVector = Icons.Default.Share, contentDescription = "Share bookmark"
                )
            }
        }
    })
}