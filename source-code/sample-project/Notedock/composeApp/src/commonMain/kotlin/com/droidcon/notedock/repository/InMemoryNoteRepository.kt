package com.droidcon.notedock.repository

import androidx.compose.runtime.mutableStateListOf
import com.droidcon.notedock.model.Note
import com.droidcon.notedock.util.Utils

class InMemoryNoteRepository : NoteRepository {
    // This mutableStateListOf will hold our notes in memory
    // Changes to this list will trigger recompositions in Compose UIs observing it.
    private val notes = mutableStateListOf<Note>()

    // For initial demonstration, let's pre-populate some notes
    init {
        // Add some dummy data if the list is empty on creation
        if (notes.isEmpty()) {
            repeat(5) { i ->
                val id = i + 1 // Start IDs from 1
                //Create initial notes
                val sentence = Utils.generateRandomSentence()
                val title = sentence.split(' ')[0]
                notes.add(
                    Note(
                        id = id, title = title, content = sentence, timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }


    override fun getAll(): List<Note> {
        return notes.toList() // Return a copy to prevent external modification of the internal list
    }

    override fun getById(id: Int): Note? {
        return notes.find { it.id == id } // Assuming 'uid' is the unique identifier
    }

    override fun add(note: Note) {
        // Ensure unique ID or handle ID generation if not provided by caller
        val newId = (notes.maxOfOrNull { it.id } ?: 0) + 1
        notes.add(note.copy(id = newId)) // Add note, ensuring a unique ID
    }

    override fun update(note: Note) {
        val index = notes.indexOfFirst { it.id == note.id } // Use uid for finding
        if (index != -1) {
            notes[index] = note // Update the note at its found index
        }
    }

    override fun delete(note: Note) {
        notes.removeAll { it.id == note.id } // Remove all notes with the given uid
    }
}