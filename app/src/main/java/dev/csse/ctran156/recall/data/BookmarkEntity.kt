package dev.csse.ctran156.recall.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.csse.ctran156.recall.BookmarkNote

@Entity
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "created") var creationTime: Long = System.currentTimeMillis(),

    var name: String = "",
    val description: String? = ""
)
