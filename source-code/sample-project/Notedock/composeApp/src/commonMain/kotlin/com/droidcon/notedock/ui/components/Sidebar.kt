@file:OptIn(ExperimentalMaterial3Api::class)

package com.droidcon.notedock.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.v2.ScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.DragAndDropTransferable
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.droidcon.notedock.model.Note
import com.droidcon.notedock.util.convertTimestampToDateString
import com.droidcon.notedock.util.dashedBorder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

//We put Sidebar under desktopMain because it requires desktop-specific APIs
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Sidebar(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    selectedNote: Note?,
    onSelectNote: (Note?) -> Unit,
    onNoteHover: (Note) -> Unit,
    onNoteDelete: (Int) -> Unit,
    onNewNote: () -> Unit,
    onOpenRandomJoke: () -> Unit,
    onShowMessage: (String) -> Unit
    ){
    Box (modifier){
        val listState = rememberLazyListState()
        val textMeasurer = rememberTextMeasurer()
        var boxBorderSize by remember{ mutableStateOf(0.dp) }
        var hoverOffset by remember { mutableStateOf(-1) }

        LazyColumn(
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.fillMaxSize()
                .padding(8.dp)
                .border(2.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.large)
        ) {
            //New note button
            item {
                TooltipArea(tooltip = {
                    Surface(Modifier.shadow(elevation = 4.dp, shape = MaterialTheme.shapes.small)){
                        Text("Create a new note", Modifier.padding(4.dp))
                    }
                }, delayMillis = 500){
                    Button({ onNewNote() }, modifier = Modifier.padding(8.dp)){
                        Icon(Icons.Outlined.Add, contentDescription = "Add")
                        Text("New Note")
                    }
                }
            }

            item {
                TooltipArea(tooltip = {
                    Surface(Modifier.shadow(elevation = 4.dp, shape = MaterialTheme.shapes.small)){
                        Text("Get a random joke from server", Modifier.padding(4.dp))
                    }
                }, delayMillis = 500){
                    Button({ onOpenRandomJoke() }, modifier = Modifier.padding(8.dp)){
                        Icon(Icons.Outlined.CalendarToday, "Today")
                        Text("Random Joke", Modifier.padding(8.dp))
                    }
                }
            }

            item { Spacer(Modifier.height(2.dp).fillMaxWidth().background(MaterialTheme.colorScheme.onSurfaceVariant)) }
            itemsIndexed(items = notes, key = {index:Int, note:Note -> note.id}) { index, note ->
                TooltipArea(tooltip = {
                    Surface(Modifier.shadow(4.dp, shape = MaterialTheme.shapes.small), contentColor = TooltipDefaults.plainTooltipContentColor, color = TooltipDefaults.plainTooltipContainerColor){
                        if (hoverOffset == index){
                            Text("Drag to copy text to another window", Modifier.align(Alignment.Center).padding(8.dp), style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }, delayMillis = 500) {
                    Box(Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            //Select if not already selected, otherwise de-select
                            if (note.id == selectedNote?.id)
                                onSelectNote(null)
                            else onSelectNote(note)

                        })
                        .background(if (note.id == selectedNote?.id) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.background, MaterialTheme.shapes.small)
                        .composed{
                            if (hoverOffset == index)
                                dashedBorder(SolidColor(MaterialTheme.colorScheme.onTertiaryContainer), shape = MaterialTheme.shapes.small, strokeWidth = boxBorderSize)
                            else Modifier
                        }
                        .padding(8.dp)
                        .onPointerEvent(PointerEventType.Enter){
                            boxBorderSize = 4.dp
                            print("PointerEventType.Enter")
                            hoverOffset = index
                        }
                        .onPointerEvent(PointerEventType.Exit){
                            boxBorderSize = 0.dp
                            print("PointerEventType.Exit")
                            hoverOffset = -1
                        }
                        .dragAndDropSource(
                            drawDragDecoration = {
                                drawRect(
                                    color = Color.Gray,
                                    topLeft = Offset(0f, size.height),
                                    size = Size(size.width, size.height)
                                )
                                val textLayoutResult = textMeasurer.measure(
                                    text = AnnotatedString(note.content),
                                    layoutDirection = layoutDirection,
                                    density = this
                                )

                                drawText(
                                    textLayoutResult = textLayoutResult,
                                    topLeft = Offset(20f, 20f)
                                )

                            }
                        ){offset ->
                            DragAndDropTransferData(
                                transferable = DragAndDropTransferable(
                                    StringSelection(note.content)
                                ),
                                supportedActions = listOf(
                                    DragAndDropTransferAction.Copy,
                                    DragAndDropTransferAction.Move,
                                    DragAndDropTransferAction.Link
                                ),
                                dragDecorationOffset = offset,
                                onTransferCompleted = {
                                    println("Action at source: $it")
                                }
                            )
                        }

                    ) {
                        Column {
                            Text(text = note.title, modifier = Modifier.padding(4.dp))
                            Text(text = note.content, modifier = Modifier.padding(4.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(text = convertTimestampToDateString(note.timestamp, "MM/dd/yyyy HH:mm:ss"),
                                style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
//        VerticalScrollbar(adapter = object: ScrollbarAdapter{
//            override val scrollOffset: Double
//                get() = TODO("Not yet implemented")
//            override val contentSize: Double
//                get() = TODO("Not yet implemented")
//            override val viewportSize: Double
//                get() = TODO("Not yet implemented")
//
//            override suspend fun scrollTo(scrollOffset: Double) {
//                TODO("Not yet implemented")
//            }
//        },
////            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
//        )
    }
}