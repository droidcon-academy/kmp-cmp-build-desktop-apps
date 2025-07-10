@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Notification
import com.droidcon.notedock.model.Note

import com.droidcon.notedock.ui.components.PreviewPane
import com.droidcon.notedock.ui.components.Sidebar
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun NotesScreen(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    selectedNote: Note?, // Pass selected note to display in preview
    onNewNote: () -> Unit,
    onEditNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onOpenRandomJoke: () -> Unit,
    onSelectNote: (Note?) -> Unit, // Callback for selecting/deselecting a note in the list
    onSelectPrevNote: (Note) -> Unit,
    onSelectNextNote: (Note) -> Unit,

) {
    //Used for showing Snackbar messages
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }) {paddingValues ->
        Row(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Notes List Sidebar with Scrollbar
            Sidebar(Modifier.fillMaxWidth(0.3f),
                notes = notes,
                selectedNote = selectedNote,
                onSelectNote = onSelectNote,
                onDeleteNote = onDeleteNote,
                onNewNote = onNewNote,
                onOpenRandomJoke = { onOpenRandomJoke() },
                onShowMessage = {
                   scope.launch { snackBarHostState.showSnackbar(it) }
                },
                onSelectPrevNote = onSelectPrevNote,
                onSelectNextNote = onSelectNextNote,
            )

            // Note Preview Pane
            PreviewPane(selectedNote,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.extraLarge)
                ,
                onEditNote = onEditNote
            )
        }

    }
}