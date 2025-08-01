package com.droidcon.notedock.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.droidcon.notedock.model.Note


@Composable
fun DeleteConfirmationScreen(
    modifier: Modifier = Modifier,
    note: Note,
    onCloseRequest: () -> Unit,
    onConfirmDelete: (Note?) -> Unit
) {

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Are you sure you want to delete the note titled \"${note.title}?\"", Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )
        Text(
            note.content, Modifier.padding(16.dp),
            maxLines = 2, overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )

        Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.Center) {
            //No button
            Button({
                onConfirmDelete(null)
                onCloseRequest() //Cancel and send request for closing the dialog
            }, Modifier.padding(8.dp)) {
                Text("No")
            }

            //Yes button
            Button(
                {
                    onConfirmDelete(note)
                    onCloseRequest() // Confirm and close
                }, Modifier.padding(8.dp)
            ) {
                Text("Yes")
            }
        }
    }
}