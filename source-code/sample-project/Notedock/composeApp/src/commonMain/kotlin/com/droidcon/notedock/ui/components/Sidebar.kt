@file:OptIn(ExperimentalMaterial3Api::class)

package com.droidcon.notedock.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.DragAndDropTransferable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.droidcon.notedock.model.Note
import com.droidcon.notedock.util.convertTimestampToDateString
import java.awt.datatransfer.StringSelection

//We put Sidebar under desktopMain because it requires desktop-specific APIs
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Sidebar(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    selectedNote: Note?,
    onSelectNote: (Note?) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onNewNote: () -> Unit,
    onOpenRandomJoke: () -> Unit,
    onSelectPrevNote: (Note) -> Unit,
    onSelectNextNote: (Note) -> Unit
) {
    Box(modifier) {
        val listState = rememberLazyListState()
        val textMeasurer = rememberTextMeasurer()
        var hoverOffset by remember { mutableStateOf(-1) }

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.fillMaxSize().padding(8.dp)
                .border(2.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.large)
        ) {
            //New note button
            item {
                TooltipArea(tooltip = {
                    Surface(Modifier.shadow(elevation = 4.dp, shape = MaterialTheme.shapes.small)) {
                        Text(
                            "Create a new note (Ctrl + N)",
                            Modifier.padding(4.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }, delayMillis = 500) {
                    Button({ onNewNote() }, modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Outlined.Add, contentDescription = "Add")
                        Text("New Note")
                    }
                }
            }

            item {
                TooltipArea(tooltip = {
                    Surface(Modifier.shadow(elevation = 4.dp, shape = MaterialTheme.shapes.small)) {
                        Text(
                            "Get a random joke from server (Ctrl + Shift + D)",
                            Modifier.padding(4.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }, delayMillis = 500) {
                    Button({ onOpenRandomJoke() }, modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Outlined.CalendarToday, "Today")
                        Text("Random Joke", Modifier.padding(8.dp))
                    }
                }
            }

            item { Spacer(Modifier.height(2.dp).fillMaxWidth().background(MaterialTheme.colorScheme.onSurfaceVariant)) }
            itemsIndexed(items = notes, key = { index: Int, note: Note -> note.id }) { index, note ->

                Box(
                    Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                        //Select if not already selected, otherwise de-select
                        if (note.id == selectedNote?.id) onSelectNote(null)
                        else onSelectNote(note)

                        }
                        ).background(
                        if (note.id == selectedNote?.id) Color.LightGray else MaterialTheme.colorScheme.background,
                        MaterialTheme.shapes.small
                    ).border(
                        1.dp,
                        if (hoverOffset == index) Color.Red else Color.Transparent,
                        shape = MaterialTheme.shapes.medium
                    ).padding(8.dp).onPointerEvent(PointerEventType.Enter) {
                        println("PointerEventType.Enter")
                        hoverOffset = index
                    }.onPointerEvent(PointerEventType.Exit) {
                        println("PointerEventType.Exit")
                        hoverOffset = -1
                    }.dragAndDropSource(
                        drawDragDecoration = {
                            drawRect(
                                color = Color.Gray, topLeft = Offset(0f, 0f), size = Size(size.width, size.height)
                            )
                            val textLayoutResult = textMeasurer.measure(
                                text = AnnotatedString(note.content),
                                layoutDirection = layoutDirection,
                                density = this
                            )

                            drawText(
                                textLayoutResult = textLayoutResult, topLeft = Offset(20f, 20f)
                            )

                        }) { offset ->
                        DragAndDropTransferData(
                            transferable = DragAndDropTransferable(
                                StringSelection(note.content)
                            ), supportedActions = listOf(
                                DragAndDropTransferAction.Copy,
                                DragAndDropTransferAction.Move,
                                DragAndDropTransferAction.Link
                            ), dragDecorationOffset = offset, onTransferCompleted = {
                                println("Action at source: $it")
                            })
                    }.onKeyEvent { event -> //Handles note selection with up/down keys

                        if (event.type == KeyEventType.KeyDown) {  // Select next and previous notes
                            selectedNote?.let {
                                val handled = when (event.key) {
                                    Key.DirectionUp -> {
                                        onSelectPrevNote(selectedNote); true
                                    }

                                    Key.DirectionDown -> {
                                        onSelectNextNote(selectedNote); true
                                    }

                                    Key.Delete, Key.Backspace -> {
                                        onDeleteNote(selectedNote); true
                                    }

                                    else -> false
                                }
                                if (handled) return@onKeyEvent true
                            }
                            true // Important! Ensures the key event is not consumed multiple times
                        } else false // If it's not a KeyDown, or selected note is null or the key was not handled by us, propagate it
                    }

                ) {

                    Column {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(text = note.title, modifier = Modifier.padding(4.dp).align(Alignment.CenterStart))
                            TooltipArea(tooltip = {
                                Surface(
                                    modifier = Modifier.shadow(2.dp, shape = MaterialTheme.shapes.small),
                                    color = TooltipDefaults.plainTooltipContainerColor,
                                    contentColor = TooltipDefaults.plainTooltipContentColor
                                ) {
                                    Text(
                                        "Delete this note (Del)",
                                        Modifier.padding(8.dp),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }, modifier = Modifier.padding(4.dp).align(Alignment.CenterEnd)) {
                                IconButton({
                                    onSelectNote(note) //Select if not already selected
                                    onDeleteNote(note)
                                }) {
                                    Icon(Icons.Outlined.Delete, "Delete")
                                }
                            }
                        }
                        TooltipArea(tooltip = {
                            Surface(
                                Modifier.shadow(4.dp, shape = MaterialTheme.shapes.small),
                                contentColor = TooltipDefaults.plainTooltipContentColor,
                                color = TooltipDefaults.plainTooltipContainerColor
                            ) {
                                if (hoverOffset == index) {
                                    Text(
                                        "${note.content.take(50)}...\nHold and drag to copy text to another window",
                                        modifier = Modifier.padding(8.dp),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }, delayMillis = 500) {
                            Text(
                                text = note.content,
                                modifier = Modifier.padding(4.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = convertTimestampToDateString(note.timestamp, "MM/dd/yyyy HH:mm:ss"),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        VerticalScrollbar(
            adapter = ScrollbarAdapter(listState)
        )

    }
}