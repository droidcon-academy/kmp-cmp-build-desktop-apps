package com.droidcon.notedock.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import com.droidcon.notedock.model.Note
import com.droidcon.notedock.ui.components.NoteEditorScreen
import com.droidcon.notedock.util.Utils

@Composable
fun NoteEditorWindow(
    winTitle: String,
    note: Note?, onClose: () -> Unit,
    onSave: (Note) -> Unit,
) {



    Window(
        title = winTitle,
        onCloseRequest = onClose) {
        NoteEditorScreen(
            note = note,
            onClose = onClose,
            onSave = onSave
        )
    }
}