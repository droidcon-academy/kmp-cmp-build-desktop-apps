package com.droidcon.notedock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.notedock.model.Note
import com.droidcon.notedock.repository.NoteRepository
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

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            val items = repository.getAll()
            val index = items.indexOf(note)
            if ( index + 1 < items.size) selectNote(items[index+1]) //Select the next item if it's not the last item in the list
            else if (index > 0) selectNote(items[index-1])  //Select previous item if the item is the last one in the list
            else selectNote(null) //If it's the only item in the list, de-select it, because it's getting deleted
            repository.delete(note)
            loadNotes() // Refresh the list after deletion
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


    /**
     * Selects the note previous to this note.
     * If the selected note is the first in the list, no action will be taken
     */
    fun selectPrevNote() {
        val selectedIndex = _notes.value.indexOfFirst { it.id == _selectedNote.value?.id }
        if (selectedIndex > 0) selectNote(_notes.value[selectedIndex - 1])
    }

    /**
     * Selects the note after this note.
     * If the current selected note is the last in the list, no action will be taken
     */
    fun selectNextNote() {
        val selectedIndex = _notes.value.indexOfFirst { it.id == _selectedNote.value?.id }
        if (selectedIndex < _notes.value.size - 1 && selectedIndex >= 0) selectNote(_notes.value[selectedIndex + 1])
    }


}