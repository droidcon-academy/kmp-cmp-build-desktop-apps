package com.droidcon.notedock

import com.droidcon.notedock.model.Note
import com.droidcon.notedock.repository.InMemoryNoteRepository
import com.droidcon.notedock.repository.NoteRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InMemoryNoteRepositoryTest {

    // Declare repository as var and initialize it in @BeforeTest
    // This ensures a fresh repository for each test, preventing state leakage between tests.
    private lateinit var repository: NoteRepository

    @BeforeTest
    fun setup() {
        // Initialize a fresh InMemoryNoteRepository before each test function runs
        repository = InMemoryNoteRepository()
        // Empty repository initially
        repository.getAll().forEach { repository.delete(it) }
    }

    @Test
    fun testAddNote() {
        val note1 = Note(1, "Title 1", "Content 1")
        repository.add(note1)

        val fetchedNote = repository.getById(note1.id)
        assertNotNull(fetchedNote, "Note should not be null after adding")
        assertEquals(note1.id, fetchedNote.id, "Fetched note ID should match")
        assertEquals(note1.title, fetchedNote.title, "Fetched note title should match")
        assertEquals(note1.content, fetchedNote.content, "Fetched note content should match")
        assertEquals(1, repository.getAll().size, "Repository should contain one note after adding")
    }

    @Test
    fun testAddMultipleNotes() {
        val note1 = Note(1, "Title 1", "Content 1")
        val note2 = Note(2, "Title 2", "Content 2")
        repository.add(note1)
        repository.add(note2)

        assertEquals(2, repository.getAll().size, "Repository should contain two notes after adding multiple")
        assertTrue(repository.getAll().contains(note1), "Repository should contain note1")
        assertTrue(repository.getAll().contains(note2), "Repository should contain note2")
    }



    @Test
    fun testGetByIdNonExistentNote() {
        val fetchedNote = repository.getById(99)
        assertNull(fetchedNote, "getById should return null for a non-existent note")
    }

    @Test
    fun testDeleteExistingNote() {
        val note1 = Note(1, "Title", "Content")
        repository.add(note1)
        assertEquals(1, repository.getAll().size, "Pre-condition: Repository should have 1 note")

        repository.delete(note1)
        assertFalse(repository.getAll().contains(note1), "Note should be removed from repository")
        assertEquals(0, repository.getAll().size, "Repository should be empty after deleting the only note")
        assertNull(repository.getById(note1.id), "getById should return null after note is deleted")
    }

    @Test
    fun testDeleteNonExistentNote() {
        val note1 = Note(1, "Title", "Content")
        repository.add(note1) // Add one note so repository is not empty

        val nonExistentNote = Note(99, "Non Existent", "Content")
        repository.delete(nonExistentNote) // Try to delete a note that doesn't exist

        assertEquals(1, repository.getAll().size, "Repository size should not change when deleting non-existent note")
        assertTrue(repository.getAll().contains(note1), "Existing note should still be present")
    }

    @Test
    fun testGetAllNotesEmptyRepository() {
        assertTrue(repository.getAll().isEmpty(), "getAll should return an empty list for an empty repository")
    }

    @Test
    fun testGetAllNotesWithData() {
        val note1 = Note(1, "A", "Content A")
        val note2 = Note(2, "B", "Content B")
        repository.add(note1)
        repository.add(note2)

        val allNotes = repository.getAll()
        assertEquals(2, allNotes.size, "getAll should return all notes")
        assertTrue(allNotes.contains(note1))
        assertTrue(allNotes.contains(note2))
    }

    @Test
    fun testUpdateExistingNote() {
        val originalNote = Note(1, "Old Title", "Old Content")
        repository.add(originalNote)

        val updatedNote = Note(1, "New Title", "New Content")
        repository.update(updatedNote)

        val fetchedNote = repository.getById(1)
        assertNotNull(fetchedNote)
        assertEquals(updatedNote.title, fetchedNote.title, "Note title should be updated")
        assertEquals(updatedNote.content, fetchedNote.content, "Note content should be updated")
        assertEquals(1, repository.getAll().size, "Repository size should remain 1 after update")
    }

    @Test
    fun testUpdateNonExistentNote() {
        val nonExistentNote = Note(99, "Non Existent", "Content")
        repository.update(nonExistentNote) // Attempt to update a note that doesn't exist

        assertNull(repository.getById(99), "Updating a non-existent note should not add it")
        assertTrue(repository.getAll().isEmpty(), "Repository should remain empty if update target was non-existent")
    }
}