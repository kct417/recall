package dev.csse.ctran156.recall.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UriDao {
    @Query("SELECT * FROM UriEntity WHERE id = :id")
    fun getUri(id: Long): Flow<UriEntity?>

    @Query("SELECT * FROM UriEntity WHERE bookmark_id = :bookmarkId ORDER BY uri COLLATE NOCASE")
    fun getUris(bookmarkId: Long): Flow<List<UriEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUri(entity: UriEntity): Long

    @Update
    fun updateUri(entity: UriEntity)

    @Delete
    fun deleteUri(entity: UriEntity)
}
