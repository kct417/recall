package dev.csse.ctran156.recall.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.csse.ctran156.recall.MainApplication
import dev.csse.ctran156.recall.data.BookmarkEntity
import dev.csse.ctran156.recall.data.BookmarkRepository
import dev.csse.ctran156.recall.data.NoteEntity
import dev.csse.ctran156.recall.data.TagEntity
import dev.csse.ctran156.recall.data.UriEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LibraryScreenUiState(
    val bookmarks: List<BookmarkEntity> = emptyList(),
    val uris: List<UriEntity> = emptyList(),
    val tags: List<TagEntity> = emptyList(),
    val notes: List<NoteEntity> = emptyList(),
    val selected: BookmarkEntity? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class RecallViewModel(val bookmarkRepo: BookmarkRepository) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                RecallViewModel(
                    bookmarkRepo = application.bookmarkRepo
                )
            }
        }
    }

    val tagList = mutableStateListOf("Personal", "Work", "Other")
    val selectedBookmarks = MutableStateFlow<Set<Long>>(emptySet())
    private val searchQuery = MutableStateFlow("")

    private val bookmarksFlow: Flow<List<BookmarkEntity>> = bookmarkRepo.getBookmarks()

    val filteredBookmarksFlow: Flow<List<BookmarkEntity>> =
        combine(bookmarksFlow, searchQuery) { bookmarks, query ->
            if (query.isBlank()) {
                bookmarks
            } else {
                bookmarks.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.description?.contains(query, ignoreCase = true) == true
                }
            }
        }

    private val selectedFlow = MutableStateFlow<BookmarkEntity?>(null)

    private val urisFlow = selectedFlow.flatMapLatest { bookmark ->
        if (bookmark != null) bookmarkRepo.getUris(bookmark = bookmark)
        else flowOf(emptyList())
    }

    private val tagsFlow = selectedFlow.flatMapLatest { bookmark ->
        if (bookmark != null) bookmarkRepo.getTags(bookmark = bookmark)
        else flowOf(emptyList())
    }

    private val notesFlow = selectedFlow.flatMapLatest { bookmark ->
        if (bookmark != null) bookmarkRepo.getNotes(bookmark = bookmark)
        else flowOf(emptyList())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<LibraryScreenUiState> = combine(
        filteredBookmarksFlow, urisFlow, tagsFlow, notesFlow, selectedFlow
    ) { bookmarks, uris, tags, notes, selected ->
        LibraryScreenUiState(
            bookmarks = bookmarks, uris = uris, tags = tags, notes = notes, selected = selected
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = LibraryScreenUiState()
    )

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    val selectedBookmarksExist: Boolean
        get() = selectedBookmarks.value.isNotEmpty()

    fun toggleBookmarkSelected(bookmark: BookmarkEntity) {
        val current = selectedBookmarks.value.toMutableSet()

        if (bookmark.id in current) current.remove(bookmark.id)
        else current.add(bookmark.id)

        selectedBookmarks.value = current
    }

    fun isSelected(bookmark: BookmarkEntity): Boolean {
        return bookmark.id in selectedBookmarks.value
    }

    fun selectBookmark(bookmark: BookmarkEntity) {
        selectedFlow.value = bookmark
    }

    fun findBookmarkByName(name: String): List<BookmarkEntity> {
        return uiState.value.bookmarks.filter {
            it.name.contains(name)
        }
    }

    fun findBookmarkById(id: String): BookmarkEntity? {
        return uiState.value.bookmarks.find {
            it.id.toString() == id
        }
    }

    fun addBookmark(
        name: String, uri: String, description: String, tags: List<String>
    ) {
        val bookmark = BookmarkEntity(name = name, description = description)
        val uri = if (uri.trim() != "") UriEntity(uri = uri) else null
        val tags = tags.map { TagEntity(tag = it) }

        viewModelScope.launch {
            bookmarkRepo.addBookmark(
                bookmark = bookmark, uri = uri, tags = tags
            )
        }
    }

    fun addUri(uri: String, id: Long) {
        val uri = if (uri.trim() != "") UriEntity(uri = uri, bookmarkId = id) else return
        viewModelScope.launch {
            bookmarkRepo.addUri(uri = uri)
        }
    }

    fun deleteSelectedBookmarks() {
        viewModelScope.launch {
            val bookmarks = uiState.value.bookmarks.filter { it.id in selectedBookmarks.value }
            bookmarks.forEach { bookmarkRepo.deleteBookmark(it) }
        }
    }

    fun createShareLink(): String {
        val firstUri = uiState.value.uris
            .map { it.uri.trim() }
            .firstOrNull { it.startsWith("http") }
            ?: uiState.value.uris.firstOrNull()?.uri

        return "$firstUri"
    }

}