package com.droidcon.notedock.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogWindow
import com.droidcon.notedock.model.Note
import com.droidcon.notedock.ui.components.DeleteConfirmationScreen

@Composable
fun DeleteConfirmationDialog(
    note: Note, //Note to decide about deleting
    onCloseRequest: () -> Unit,
    onConfirmNoteDeletion: (Note?) -> Unit // Inform of the user's decision
){
    DialogWindow(
        title = "Confirmation",
        onCloseRequest = onCloseRequest,

    ){
        DeleteConfirmationScreen(
            modifier = Modifier.fillMaxSize(0.5f),
            note = note,
            onCloseRequest = onCloseRequest,
            onConfirmNoteDeletion = onConfirmNoteDeletion
        )
    }
}