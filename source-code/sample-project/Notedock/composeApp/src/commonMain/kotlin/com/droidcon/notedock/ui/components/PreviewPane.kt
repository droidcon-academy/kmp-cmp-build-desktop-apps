package com.droidcon.notedock.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.droidcon.notedock.model.Note


// We are
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PreviewPane(
    note: Note?,
    onEditNote: (Note) -> Unit,
    modifier: Modifier = Modifier,
    onShowMessage: (String) -> Unit
){

    Column (modifier) {
        note?.let {
            Column(Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondaryContainer)

            ) {

                TooltipArea(tooltip = {
                    Surface(modifier = Modifier.shadow(elevation = 4.dp, shape = MaterialTheme.shapes.small)){
                        Text("Edit this note", Modifier.padding(4.dp))
                    }
                }, delayMillis = 500) {

                    IconButton(onClick = {
                        onEditNote(note)
                    }) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit Note")
                    }
                }

                Text(text = note.title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(8.dp))

                Box(Modifier.fillMaxWidth().padding(8.dp)) {
                    // Edit note button
                    Text(
                        note.content, modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }
        }
        if (note == null){
            Text("Select a note to preview", Modifier.padding(8.dp).align(Alignment.CenterHorizontally))
        }
    }

}