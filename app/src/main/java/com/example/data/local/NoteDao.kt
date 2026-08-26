package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM math_notes ORDER BY isPinned DESC, timestamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM math_notes WHERE category = :category ORDER BY isPinned DESC, timestamp DESC")
    fun getNotesByCategory(category: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM math_notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY isPinned DESC, timestamp DESC")
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Query("SELECT COUNT(*) FROM math_notes")
    suspend fun getNotesCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM math_notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long)

    @Query("UPDATE math_notes SET isPinned = NOT isPinned WHERE id = :id")
    suspend fun togglePin(id: Long)
}
