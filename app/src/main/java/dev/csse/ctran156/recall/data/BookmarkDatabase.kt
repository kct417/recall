package dev.csse.ctran156.recall.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BookmarkEntity::class, UriEntity::class, TagEntity::class, NoteEntity::class], version = 1
)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun uriDao(): UriDao
    abstract fun tagDao(): TagDao
    abstract fun NoteDao(): NoteDao
}
