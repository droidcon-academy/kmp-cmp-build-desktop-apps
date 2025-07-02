package com.droidcon.notedock.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.notedock.model.Note
import kotlinx.coroutines.launch

@Composable
fun NoteEditorScreen(
    modifier: Modifier = Modifier,
    note: Note?,
    onClose: () -> Unit,
    onSave: (Note) -> Unit
){
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth().fillMaxHeight(0.7f)) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Makes it expandable
                    .background(MaterialTheme.colorScheme.secondaryContainer)

            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onClose) { Text("Cancel") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (note == null) {
                        onSave(
                            Note(
                                id = -1,
                                title = title,
                                content = content
                            )
                        ) //Passing -1 means this is a new note rather than an edited one
                        scope.launch { snackbarHostState.showSnackbar("Created new note")}
                    } else {
                        val edited = note.copy(title = title, content = content)
                        onSave(edited)
                        scope.launch { snackbarHostState.showSnackbar("Saved note") }
                    }
                }) { Text("Save") }
            }
        }
    }
}