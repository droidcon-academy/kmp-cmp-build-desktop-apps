package com.droidcon.notedock.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window
import com.droidcon.notedock.model.Note
import com.droidcon.notedock.ui.components.NoteEditorScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteEditorWindow(
    winTitle: String,
    note: Note?, onClose: () -> Unit,
    onSave: (Note) -> Unit,
) {
    Window(
        title = winTitle,
        onCloseRequest = onClose
    ) {
        NoteEditorScreen(
            note = note,
            onClose = onClose,
            onSave = onSave
        )
    }
}