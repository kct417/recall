package dev.csse.ctran156.recall

import java.util.Date

var lastBookmarkId = 0

data class Bookmark(
    val id: Int = lastBookmarkId++,
    val name: String = "Bookmark $lastBookmarkId",
    val uri: String = "",
    val description: String? = null,
    val selected: Boolean = false,
    val tags: List<String> = listOf(),
    val notes: List<BookmarkNote> = listOf()
)

data class BookmarkNote(
    val text: String,
    val datetime: Date,
)
