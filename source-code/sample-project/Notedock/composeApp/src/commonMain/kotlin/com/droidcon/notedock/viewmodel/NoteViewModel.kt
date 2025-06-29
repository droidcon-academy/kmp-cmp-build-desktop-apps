package com.droidcon.notedock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.notedock.model.Note
import com.droidcon.notedock.repository.NoteRepository
import com.droidcon.notedock.util.AppWindow
import com.droidcon.notedock.util.WinStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())

    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote.asStateFlow()




    init {
        // Load initial notes when the ViewModel is created
        loadNotes()
    }


    private fun loadNotes() {
        viewModelScope.launch {
            _notes.value = repository.getAll()
        }
    }


    fun selectNote(note: Note?) {
        _selectedNote.value = note
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            if (repository.getById(note.id) != null) {
                repository.update(note)
                loadNotes() // Refresh the list after saving
                selectNote(note)
            } else {
                createNewNote(note.title, note.content)
            }
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            repository.delete(noteId)
            loadNotes() // Refresh the list after deletion
            if (_selectedNote.value?.id == noteId) {
                selectNote(null) // Deselect if the deleted note was selected
            }
        }
    }

    fun createNewNote(title: String = "New Note", content: String = "") {
        val newId = (notes.value.maxOfOrNull { it.id } ?: -1) + 1
        val newNote = Note(newId, title, content)
        repository.add(newNote)
        loadNotes()
        selectNote(newNote) // Immediately select the new note for editing
        print("Created note ${newNote.title}: ${newNote.content}")
    }

    // Function to update note content temporarily for editing without saving
    fun updateEditingNoteContent(noteId: Int, newTitle: String, newContent: String) {
        if (_selectedNote.value?.id == noteId) {
            _selectedNote.value = _selectedNote.value?.copy(title = newTitle, content = newContent)
        }
    }



}