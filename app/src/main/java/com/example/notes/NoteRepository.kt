package com.example.notes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteRepository (private val noteDao: NoteDao) {

    fun getAllNotesFlow(): Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun getNoteById(id: Long): Note? = noteDao.getNoteById(id)

    suspend fun addNote(note: Note): Long {
        return noteDao.addNotes(note)
    }


    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNote(id: Long) = noteDao.deleteId(id)
}