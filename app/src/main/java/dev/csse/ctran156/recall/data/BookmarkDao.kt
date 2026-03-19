package dev.csse.ctran156.recall.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM BookmarkEntity WHERE id = :id")
    fun getBookmark(id: Long): Flow<BookmarkEntity?>

    @Query("SELECT * FROM BookmarkEntity ORDER BY name COLLATE NOCASE")
    fun getBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBookmark(entity: BookmarkEntity): Long

    @Update
    fun updateBookmark(entity: BookmarkEntity)

    @Delete
    fun deleteBookmark(entity: BookmarkEntity)
}
