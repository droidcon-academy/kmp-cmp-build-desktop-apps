@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
package com.droidcon.notedock.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import com.droidcon.notedock.model.Note
import org.jetbrains.compose.resources.ExperimentalResourceApi


@OptIn(ExperimentalResourceApi::class)
@Composable
fun OldMainNotesWindow(
    notes: List<Note>,
    selectedNote: Note?, // Pass selected note to display in preview
    onOpenQuickNote: () -> Unit,
    onNewNote: () -> Unit,
    onEditNote: (Note) -> Unit,
    onDeleteNote: (Int) -> Unit,
    onOpenRandomJoke: () -> Unit,
    onCloseApp: () -> Unit,
    onSelectNote: (Note) -> Unit // Callback for selecting a note in the list
) {
    val listState = rememberLazyListState()
    val draggedIndex = remember { mutableStateOf<Int?>(null) } // Keep if you re-enable drag and drop
    val targetIndex = remember { mutableStateOf<Int?>(null) }   // Keep if you re-enable drag and drop
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val hapticFeedback = LocalHapticFeedback.current

    Window(
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        title = "Notedock",
        onCloseRequest = onCloseApp
    ) {
        MaterialTheme {
            Row(modifier = Modifier.fillMaxSize()) {
                // Notes List Sidebar with Scrollbar
                Box(modifier = Modifier.width(220.dp).fillMaxHeight()) {
                    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
                        TooltipArea(
                            tooltip = { Surface(modifier = Modifier.shadow(4.dp), shape = MaterialTheme.shapes.small, color = TooltipDefaults.plainTooltipContainerColor, contentColor = TooltipDefaults.plainTooltipContentColor) { Text("Create a new note", modifier = Modifier.padding(8.dp)) } },
                            delayMillis = 500
                        ) { Button(onClick = onNewNote, modifier = Modifier.fillMaxWidth()) { Text("New Note") } }
                        Spacer(modifier = Modifier.height(4.dp))
                        TooltipArea(
                            tooltip = { Surface(modifier = Modifier.shadow(4.dp), shape = MaterialTheme.shapes.small, color = TooltipDefaults.plainTooltipContainerColor, contentColor = TooltipDefaults.plainTooltipContentColor) { Text("Open a quick note window", modifier = Modifier.padding(8.dp)) } },
                            delayMillis = 500
                        ) { Button(onClick = onOpenQuickNote, modifier = Modifier.fillMaxWidth()) { Text("Quick Note") } }
                        Spacer(modifier = Modifier.height(4.dp))
                        TooltipArea(
                            tooltip = { Surface(modifier = Modifier.shadow(4.dp), shape = MaterialTheme.shapes.small, color = TooltipDefaults.plainTooltipContainerColor, contentColor = TooltipDefaults.plainTooltipContentColor) { Text("Get a random joke", modifier = Modifier.padding(8.dp)) } },
                            delayMillis = 500
                        ) { Button(onClick = onOpenRandomJoke, modifier = Modifier.fillMaxWidth()) { Text("Random Joke") } }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(notes, key = { it.id }) { note ->
                                    var isHovered by remember { mutableStateOf(false) }
                                    // You'll need to re-evaluate how to get index if using drag and drop
                                    val isDragging = draggedIndex.value != null && notes[draggedIndex.value!!] == note
                                    val isTarget = targetIndex.value != null && notes[targetIndex.value!!] == note
                                    val focusRequester = remember { FocusRequester() }
                                    var isFocused by remember { mutableStateOf(false) } // For focus indicator

                                    val isSelected = selectedNote?.id == note.id

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp)
                                            .background(
                                                when {
                                                    isSelected -> MaterialTheme.colors.primary.copy(alpha = 0.2f) // Highlight selected
                                                    isTarget -> Color.Green.copy(alpha = 0.3f)
                                                    isDragging -> Color.Blue.copy(alpha = 0.5f)
                                                    isHovered -> Color.LightGray
                                                    else -> Color.Transparent
                                                }
                                            )
//                                             .dragAndDropSource(...) // If keeping drag
                                            // .dragAndDropTarget(...) // If keeping drag
                                            .clickable { onSelectNote(note) } // Select on click
                                            .focusable()
                                            .onFocusEvent { focusState -> isFocused = focusState.isFocused }
                                            .border(
                                                width = if (isFocused) 2.dp else 0.dp,
                                                color = MaterialTheme.colors.primary.copy(alpha = if (isFocused) 0.6f else 0f)
                                            )
                                            .onPointerEvent(PointerEventType.Enter) { isHovered = true }
                                            .onPointerEvent(PointerEventType.Exit) { isHovered = false }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(note.title, style = MaterialTheme.typography.subtitle1)
                                                Text(note.content.take(50) + if (note.content.length > 50) "..." else "", style = MaterialTheme.typography.body2, color = Color.Gray)
                                            }
                                            IconButton(onClick = { onDeleteNote(note.id) }) {
                                                Icon(Icons.Default.Delete, "Delete Note")
                                            }
                                        }
                                    }
                                    Divider()
                                }
                            }
                            VerticalScrollbar(
                                adapter = rememberScrollbarAdapter(scrollState = listState),
                                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
                            )
                        }
                    }
                }
                // Note Preview Pane
                NotePreviewPane(selectedNote, onEditNote)
            }
        }
    }
}
