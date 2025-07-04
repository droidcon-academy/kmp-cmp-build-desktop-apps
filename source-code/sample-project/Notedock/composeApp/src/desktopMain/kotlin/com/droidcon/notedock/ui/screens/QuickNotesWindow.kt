package com.droidcon.notedock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
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
        state = rememberWindowState(width = 400.dp, height = 300.dp),
        onCloseRequest = onClose
    ) {
        var title by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }

        MaterialTheme {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Note Title", Modifier.padding(8.dp)) },
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).padding(8.dp)
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer)

                )

                TextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Note content ... ", Modifier.padding(8.dp)) },
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).padding(8.dp)
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer)

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