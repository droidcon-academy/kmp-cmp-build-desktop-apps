package com.droidcon.notedock.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.droidcon.notedock.model.Note


@Composable
fun NotePreviewPane(note: Note?, onEditNote: (Note) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (note != null) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            Text(
                text = note.content,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onEditNote(note) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Note")
            }
        } else {
            Text(
                text = "Select a note to view its content or click 'New Note' to create one.",
                style = MaterialTheme.typography.h6,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}