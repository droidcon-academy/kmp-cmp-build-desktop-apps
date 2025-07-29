package com.droidcon.notedock.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState

@Composable
fun QuickNoteWindow(
    title: String,
    onClose: () -> Unit,
    onSave: (title: String, content: String) -> Unit
) {
    Window(
        title = title,
        state = rememberWindowState(width = 500.dp, height = 400.dp),
        onCloseRequest = onClose
    ) {
        var title by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }

        //snackbar host for displaying messages if needed
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold (
            snackbarHost = { snackbarHostState }
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Note Title", Modifier.padding(8.dp)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                        .padding(8.dp)

                )

                TextField(
                    value = content,
                    onValueChange = { content = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .padding(8.dp),
                    placeholder = { Text("Note content ... ", Modifier.padding(8.dp)) }


                )

                Button({
                    onSave(title, content)
                }) {
                    Icon(Icons.Outlined.Save, "Save", modifier = Modifier.padding(2.dp))
                    Text("Save", Modifier.padding(8.dp))

                }
            }
        }
    }
}