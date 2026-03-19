package dev.csse.ctran156.recall.data

import android.content.Context
import androidx.compose.ui.platform.DisableContentCapture
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkRepository(context: Context) {
    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
        }
    }

    private val database: BookmarkDatabase = Room.databaseBuilder(
        context, BookmarkDatabase::class.java, "bookmark.db"
    ).addCallback(databaseCallback).build()

    private val bookmarkDao = database.bookmarkDao()
    private val uriDao = database.uriDao()
    private val tagDao = database.tagDao()
    private val noteDao = database.NoteDao()

    fun getBookmarks() = bookmarkDao.getBookmarks()
    fun getUris(bookmark: BookmarkEntity) = uriDao.getUris(bookmarkId = bookmark.id)
    fun getTags(bookmark: BookmarkEntity) = tagDao.getTags(bookmarkId = bookmark.id)
    fun getNotes(bookmark: BookmarkEntity) = noteDao.getNotes(bookmarkId = bookmark.id)

    fun addBookmark(
        bookmark: BookmarkEntity, uri: UriEntity?, tags: List<TagEntity>
    ) {
        if (bookmark.name.trim() != "") {
            CoroutineScope(Dispatchers.IO).launch {
                bookmark.id = bookmarkDao.addBookmark(entity = bookmark)

                uri?.let {
                    uri.bookmarkId = bookmark.id
                    uriDao.addUri(entity = uri)
                }

                tags.forEach { tag ->
                    tag.bookmarkId = bookmark.id
                    tagDao.addTag(entity = tag)
                }
            }
        }
    }

    fun deleteBookmark(
        bookmark: BookmarkEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            bookmarkDao.deleteBookmark(entity = bookmark)
        }
    }

    fun addUri(
        uri: UriEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            uriDao.addUri(entity = uri)
        }
    }

    fun addNote(
        note: NoteEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.addNote(entity = note)
        }
    }
}
