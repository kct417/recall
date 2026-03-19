package dev.csse.ctran156.recall.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM TagEntity WHERE id = :id")
    fun getTag(id: Long): Flow<TagEntity?>

    @Query("SELECT * FROM TagEntity WHERE bookmark_id = :bookmarkId ORDER BY tag COLLATE NOCASE")
    fun getTags(bookmarkId: Long): Flow<List<TagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTag(entity: TagEntity): Long

    @Update
    fun updateTag(entity: TagEntity)

    @Delete
    fun deleteTag(entity: TagEntity)
}
