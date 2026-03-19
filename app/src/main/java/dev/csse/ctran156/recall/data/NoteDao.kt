package dev.csse.ctran156.recall.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM NoteEntity WHERE id = :id")
    fun getNote(id: Long): Flow<NoteEntity?>

    @Query("SELECT * FROM NoteEntity WHERE bookmark_id = :bookmarkId ORDER BY date COLLATE NOCASE")
    fun getNotes(bookmarkId: Long): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(entity: NoteEntity): Long

    @Update
    fun updateNote(entity: NoteEntity)

    @Delete
    fun deleteNote(entity: NoteEntity)
}
