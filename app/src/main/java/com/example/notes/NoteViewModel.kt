package com.example.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    var isDarkTheme by mutableStateOf(false)
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }


    val allNotes: Flow<List<Note>> = repository.getAllNotesFlow()

    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote.asStateFlow()

    fun loadNote(id: Long) {
        viewModelScope.launch {
            _currentNote.value = repository.getNoteById(id)
        }
    }

    fun saveNote(title: String, content: String) {
        viewModelScope.launch {
            _currentNote.value?.let { existingNote ->
                val updatedNote = existingNote.copy(
                    title = title.ifEmpty { content.take(40) },
                    content = content
                )
                repository.updateNote(updatedNote)
                _currentNote.value = updatedNote
            } ?: run {
                val newNote = Note(
                    id = 0,
                    title = title.ifEmpty { content.take(40) },
                    content = content
                )
                val newId = repository.addNote(newNote)
                _currentNote.value = newNote.copy(id = newId)


            }
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
            if (_currentNote.value?.id == id) {
                _currentNote.value = null
            }
        }
    }
    fun resetCurrentNote() {
        _currentNote.value = null
    }

    val _tempTitle = mutableStateOf("")
    val _tempContent = mutableStateOf("")
    fun updateTempValues(title: String, content: String) {
        _tempTitle.value = title
        _tempContent.value = content
    }
}