package com.droidcon.notedock.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.window.DialogWindow
import com.droidcon.notedock.model.Note
import com.droidcon.notedock.ui.components.DeleteConfirmationScreen

@Composable
fun DeleteConfirmationDialog(
    note: Note, //Note to decide about deleting
    onCloseRequest: () -> Unit,
    onConfirmDelete: (Boolean) -> Unit // Inform of the user's decision
){
    DialogWindow(
        title = "Confirmation",
        onCloseRequest = onCloseRequest,
        onPreviewKeyEvent = {event-> //handle Esc key to close dialog
            if (event.type == KeyEventType.KeyDown){
                val consumed = if (event.key == Key.Escape){
                    onCloseRequest()
                    true
                }
                else {false}
                return@DialogWindow consumed
            }
            return@DialogWindow false
        }

    ){
        DeleteConfirmationScreen(
            modifier = Modifier.fillMaxSize(),
            note = note,
            onCloseRequest = onCloseRequest,
            onConfirmDelete = onConfirmDelete
        )
    }
}