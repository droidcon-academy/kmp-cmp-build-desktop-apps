@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)

package com.droidcon.notedock.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.droidcon.notedock.repository.InMemoryNoteRepository
import com.droidcon.notedock.ui.components.NotesScreen
import com.droidcon.notedock.ui.theme.NotedockTheme
import com.droidcon.notedock.util.handleMainWindowKbShortcuts
import com.droidcon.notedock.viewmodel.NoteViewModel
import io.ktor.client.*
import org.jetbrains.compose.resources.ExperimentalResourceApi


@OptIn(ExperimentalResourceApi::class)
@Composable
fun MainNotesWindow(
    isQuickNoteWindowOpen: Boolean = false,
    onCloseQuickNote: () -> Unit,
    windowState: WindowState = rememberWindowState(),
    onCloseApp: () -> Unit,
    httpClient: HttpClient
) {

    var isJokeWindowOpen by remember { mutableStateOf(false) }
    var isEditorWindowOpen by remember { mutableStateOf(false) }
    var isConfirmationDialogOpen by remember { mutableStateOf(false) }

    NotedockTheme {
        Window(
            state = windowState,
            title = "Note Dock App",
            onCloseRequest = onCloseApp,
            onPreviewKeyEvent = { event -> //Handle window-level keyboard shortcuts
                handleMainWindowKbShortcuts(
                    event,
                    onOpenNewNoteWindow = { isEditorWindowOpen = true },
                    onOpenJokeWindow = { isJokeWindowOpen = true }
                )
            }

        ) {
            val noteViewModel = viewModel { NoteViewModel(InMemoryNoteRepository()) }


            val notes by noteViewModel.notes.collectAsState()
            val selectedNote by noteViewModel.selectedNote.collectAsState()

            if (isJokeWindowOpen) {
                JokeWindow(
                    title = "Joke of the day",
                    onCloseRequest = {
                        isJokeWindowOpen = false
                    },
                    httpClient
                )
            }

            if (isEditorWindowOpen) {
                NoteEditorWindow(
                    winTitle = selectedNote?.title ?: "New Note",
                    note = selectedNote,
                    onClose = {
                        isEditorWindowOpen = false
                    },
                    onSave = {
                        noteViewModel.saveNote(it)
                        print("Inside MainNotesWindow. Saved ${it.id}, ${it.title}, ${it.content}")
                    }
                )
            }

            if (isQuickNoteWindowOpen) {
                QuickNoteWindow(
                    title = "Take a quick note",
                    onClose = { onCloseQuickNote() },
                    onSave = { title, content -> noteViewModel.createNewNote(title = title, content = content) }
                )
            }

            if (isConfirmationDialogOpen) {
                DeleteConfirmationDialog(
                    note = selectedNote!!,
                    onCloseRequest = {
                        isConfirmationDialogOpen = false
                    },
                    onConfirmDelete = { note ->
                        note?.let { noteViewModel.deleteNote(it) }
                    }
                )
            }

            NotesScreen(
                notes = notes,
                selectedNote = selectedNote,
                onNewNote = {
                    isEditorWindowOpen = true
                    noteViewModel.selectNote(null)
                },
                onEditNote = {
                    isEditorWindowOpen = true
                    noteViewModel.selectNote(it)
                },
                onDeleteNote = {
                    isConfirmationDialogOpen = true
                },
                onOpenRandomJoke = {
                    isJokeWindowOpen = true
                },
                onSelectNote = { noteViewModel.selectNote(it) },
                onSelectPrevNote = { note -> noteViewModel.selectPrevNote() },
                onSelectNextNote = { note -> noteViewModel.selectNextNote() }
            )

        }
    }
}
