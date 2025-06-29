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


@OptIn(ExperimentalResourceApi::class)
@Composable
fun MainNotesWindow(
    openQuickNote: Boolean = false,
    onCloseQuickNote: () -> Unit,
    windowState: WindowState = rememberWindowState(),
    onCloseApp: () -> Unit
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
        title = "Notedock",
        onCloseRequest = onCloseApp
    ) {
        val vm = viewModel { NoteViewModel(InMemoryNoteRepository()) }


        val notes by vm.notes.collectAsState()
        val selectedNote by vm.selectedNote.collectAsState()

        var openJokeWindow by remember{mutableStateOf(false)}
        var openEditorWindow by remember { mutableStateOf(false) }


        if (openJokeWindow){
            JokeWindow(
                title = "Joke of the day",
                onCloseRequest = {
                openJokeWindow = false
            })
        }

        if (openEditorWindow){
            NoteEditorWindow(
                winTitle = selectedNote?.title ?: "New Note",
                note = selectedNote,
                onClose = {
                   openEditorWindow = false
                },
                onSave = {
                    vm.saveNote(it)
                    print("Inside MainNotesWindow. Saved ${it.id}, ${it.title}, ${it.content}")
                }
            )
        }

        if (openQuickNote){
            QuickNoteWindow(
                title = "Take a quick note",
                onClose = { onCloseQuickNote() },
                onSave = { title, content -> vm.createNewNote(title = title, content = content)}
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
                    openEditorWindow = true
                    vm.selectNote(null)
                            },
                onEditNote = {
                    openEditorWindow = true
                    vm.selectNote(it)
                             },
                onDeleteNote = {
                    vm.deleteNote(it)

                               },
                onOpenRandomJoke = {
                    openJokeWindow = true
                                   },
                onSelectNote = { vm.selectNote(it) }
            )

        }
    }
}
