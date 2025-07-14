package com.droidcon.notedock.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropTarget
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
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.droidcon.notedock.model.Note
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.datatransfer.DataFlavor

@OptIn(ExperimentalComposeUiApi::class)
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

    //For drop target
    var targetText by remember { mutableStateOf("Drop Here") }

    // Get focus manager used to handle Tab key presses
    val focusManager = LocalFocusManager.current

    //Define focus requesters to attach for each component
    val titleFocusRequester = remember { FocusRequester() }
    val contentFocusRequester = remember { FocusRequester() }
    val cancelFocusRequester = remember { FocusRequester() }
    val saveFocusRequester = remember { FocusRequester() }


    val dragAndDropTarget = remember {
        object: DragAndDropTarget{
            override fun onStarted(event: DragAndDropEvent) {
                targetText = "Drop Here"
            }

            override fun onEnded(event: DragAndDropEvent) {
                //Do nothing
            }


            override fun onDrop(event: DragAndDropEvent): Boolean {
                println("Action in the target: ${event.action}")
                content = targetText

                targetText = event.awtTransferable.let{
                    if (it.isDataFlavorSupported(DataFlavor.stringFlavor))
                        it.getTransferData(DataFlavor.stringFlavor) as String
                    else
                        it.transferDataFlavors.first().humanPresentableName
                }

                content = targetText
                return true
            }

        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .onPreviewKeyEvent{event->
                if (event.type == KeyEventType.KeyDown && event.key == Key.Tab) {
                    if (event.isShiftPressed) {
                        // Shift + Tab: Move focus backward
                        focusManager.moveFocus(FocusDirection.Previous)
                    } else {
                        // Tab: Move focus forward
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                    true // Consume the event so the TextField does NOT get it for indentation
                } else {
                    false // Not a Tab key or not KeyDown, let it propagate normally
                }


            }
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .focusRestorer(titleFocusRequester)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                placeholder = {
                    Text("Type or drop some text here", Modifier.padding(8.dp))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Makes it expandable
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .dragAndDropTarget(shouldStartDragAndDrop = {true}, target = dragAndDropTarget)
                    .focusRequester(contentFocusRequester)


            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {


                Button(onClick = onClose,
                    modifier = Modifier
                        .focusRequester(cancelFocusRequester)
                    ) { Text("Cancel") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (title.isEmpty() && content.isEmpty()){
                        scope.launch { snackbarHostState.showSnackbar("Please fill in title and content") }
                        return@Button
                    }
                    if (note == null) {
                        onSave(
                            Note(
                                id = -1,
                                title = title,
                                content = content
                            )
                        ) //Passing -1 means this is a new note rather than an edited one
                        scope.launch { snackbarHostState.showSnackbar("Created new note") }
                    } else {
                        val edited = note.copy(title = title, content = content)
                        onSave(edited)
                        scope.launch { snackbarHostState.showSnackbar("Saved note") }
                    }
                }, modifier = Modifier
                    .focusRequester(saveFocusRequester)
                ) {
                    Text("Save")
                }

            }
        }
    }
}