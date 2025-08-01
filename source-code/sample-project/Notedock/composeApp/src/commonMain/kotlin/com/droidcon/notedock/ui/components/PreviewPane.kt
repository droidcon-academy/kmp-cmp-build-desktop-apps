package com.droidcon.notedock.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.droidcon.notedock.model.Note


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PreviewPane(
    note: Note?,
    onEditNote: (Note) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier,
        verticalArrangement = Arrangement.Center
    ) {
        note?.let {
            Column(
                Modifier
                    .fillMaxSize()

            ) {

                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    TooltipArea(tooltip = {
                        Surface(modifier = Modifier.shadow(elevation = 4.dp, shape = MaterialTheme.shapes.small)) {
                            Text("Edit this note", Modifier.padding(4.dp))
                        }
                    }, delayMillis = 500) {

                        IconButton(
                            onClick = {
                                onEditNote(note)
                            }, modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Edit Note")
                        }
                    }


                    Text(
                        text = note.title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier
                            .padding(8.dp)
                        ,
                        textAlign = TextAlign.Center
                    )

                }
                Box(Modifier.fillMaxWidth().padding(8.dp)) {
                    // Edit note button
                    Text(
                        note.content, modifier = Modifier
                            .padding(8.dp)

                    )
                }
            }
        }
        if (note == null) {
            Text("Select a note to preview", Modifier.padding(8.dp).align(Alignment.CenterHorizontally))
        }
    }

}