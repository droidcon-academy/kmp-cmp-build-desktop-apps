package com.droidcon.notedock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.notedock.model.Note
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun NotesScreen(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    selectedNote: Note?, // Pass selected note to display in preview
    onNewNote: () -> Unit,
    onEditNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onOpenRandomJoke: () -> Unit,
    onSelectNote: (Note?) -> Unit, // Callback for selecting/deselecting a note in the list
    onSelectPrevNote: (Note) -> Unit,
    onSelectNextNote: (Note) -> Unit,

    ) {
    //Used for showing Snackbar messages
    val snackBarHostState = remember { SnackbarHostState() }


    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        Row(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Notes List Sidebar with Scrollbar
            Sidebar(
                Modifier.fillMaxWidth(0.3f),
                notes = notes,
                selectedNote = selectedNote,
                onSelectNote = onSelectNote,
                onDeleteNote = onDeleteNote,
                onNewNote = onNewNote,
                onOpenRandomJoke = { onOpenRandomJoke() },
                onSelectPrevNote = onSelectPrevNote,
                onSelectNextNote = onSelectNextNote,
            )

            // Note Preview Pane
            PreviewPane(
                selectedNote,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(
                        if (selectedNote != null) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.background
                    )
                ,
                onEditNote = onEditNote
            )
        }

    }
}