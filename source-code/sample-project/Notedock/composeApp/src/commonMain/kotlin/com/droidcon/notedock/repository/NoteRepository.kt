package com.droidcon.notedock.repository // Adjust your package name

import com.droidcon.notedock.model.Note

interface NoteRepository {
    fun getAll(): List<Note>
    fun getById(id: Int): Note?
    fun add(note: Note)
    fun update(note: Note)
    fun delete(id: Int)
}