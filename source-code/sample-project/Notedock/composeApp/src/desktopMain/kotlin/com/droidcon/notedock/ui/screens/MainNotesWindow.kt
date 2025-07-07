@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
package com.droidcon.notedock.ui.screens

import androidx.compose.foundation.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.droidcon.notedock.repository.InMemoryNoteRepository
import com.droidcon.notedock.viewmodel.NoteViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi

import NotesScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Notification


@OptIn(ExperimentalResourceApi::class)
@Composable
fun MainNotesWindow(
    isQuickNoteWindowOpen: Boolean = false,
    onCloseQuickNote: () -> Unit,
    windowState: WindowState = rememberWindowState(),
    onCloseApp: () -> Unit,
    onShowNotification: (Notification) -> Unit
) {

//    val listState = rememberLazyListState()
//    val draggedIndex = remember { mutableStateOf<Int?>(null) } // Keep if you re-enable drag and drop
//    val targetIndex = remember { mutableStateOf<Int?>(null) }   // Keep if you re-enable drag and drop
//    val textMeasurer = rememberTextMeasurer()
//    val density = LocalDensity.current
//    val layoutDirection = LocalLayoutDirection.current
//    val hapticFeedback = LocalHapticFeedback.current

    Window(
        state = windowState,
        title = "Notedock Note Keeper",
        onCloseRequest = onCloseApp
    ) {
        val noteViewModel = viewModel { NoteViewModel(InMemoryNoteRepository()) }


        val notes by noteViewModel.notes.collectAsState()
        val selectedNote by noteViewModel.selectedNote.collectAsState()

        var isJokeWindowOpen by remember{mutableStateOf(false)}
        var isEditorWindowOpen by remember { mutableStateOf(false) }


        if (isJokeWindowOpen){
            JokeWindow(
                title = "Joke of the day",
                onCloseRequest = {
                isJokeWindowOpen = false
            })
        }

        if (isEditorWindowOpen){
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

        if (isQuickNoteWindowOpen){
            QuickNoteWindow(
                title = "Take a quick note",
                onClose = { onCloseQuickNote() },
                onSave = { title, content -> noteViewModel.createNewNote(title = title, content = content)}
            )
        }


//        if (notes.isEmpty()){
//            repeat(5) {
//                vm.createNewNote("Note $it", "$it note content")
//            }
//        }
        MaterialTheme {
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
                    noteViewModel.deleteNote(it)

                               },
                onOpenRandomJoke = {
                    isJokeWindowOpen = true
                                   },
                onSelectNote = { noteViewModel.selectNote(it) },
                onSelectPrevNote = {note-> noteViewModel.selectPrevNote()},
                onSelectNextNote = {note-> noteViewModel.selectNextNote() },
                onShowNotification = onShowNotification
            )

        }
    }
}
