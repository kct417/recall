package dev.csse.ctran156.recall.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.csse.ctran156.recall.data.BookmarkEntity

@Composable
fun BookmarkListScreen(
    modifier: Modifier = Modifier,
    viewModel: RecallViewModel = viewModel<RecallViewModel>(factory = RecallViewModel.Factory),
    onSelectBookmark: (BookmarkEntity) -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val bookmarks = uiState.value.bookmarks

    LazyColumn(modifier = modifier) {
        items(items = bookmarks, key = { bookmark -> bookmark.id }) { bookmark ->
            BookmarkCard(
                bookmark = bookmark,
                viewModel = viewModel,
                toggleSelected = viewModel::toggleBookmarkSelected,
                onClick = { onSelectBookmark(it) })
        }
    }
}

@Composable
fun BookmarkCard(
    bookmark: BookmarkEntity,
    modifier: Modifier = Modifier,
    viewModel: RecallViewModel,
    toggleSelected: (BookmarkEntity) -> Unit,
    onClick: (BookmarkEntity) -> Unit
) {

    val selected = viewModel.selectedBookmarks.collectAsStateWithLifecycle()

    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick(bookmark) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = bookmark.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                color = if (viewModel.isSelected(bookmark)) Color.Gray else Color.Black
            )

            Checkbox(
                checked = bookmark.id in selected.value,
                onCheckedChange = { toggleSelected(bookmark) }
            )
        }
    }
}