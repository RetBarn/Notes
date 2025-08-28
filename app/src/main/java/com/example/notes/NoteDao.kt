package com.example.notes
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query ("SELECT * FROM notes WHERE id=:id LIMIT 1")
    suspend fun getNoteById(id: Long): Note?

    @Insert (onConflict = OnConflictStrategy.ABORT)
    suspend fun addNotes(note: Note): Long

    @Update
    suspend fun updateNote(note: Note)

    @Query ("DELETE FROM notes WHERE id==:id")
    suspend fun deleteId(id:Long)
}