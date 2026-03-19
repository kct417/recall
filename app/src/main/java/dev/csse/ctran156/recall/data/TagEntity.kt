package dev.csse.ctran156.recall.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = BookmarkEntity::class,
        parentColumns = ["id"],
        childColumns = ["bookmark_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
class TagEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "bookmark_id") var bookmarkId: Long = 0L,

    var tag: String = ""
)
