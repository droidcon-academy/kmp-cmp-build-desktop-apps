package com.droidcon.notedock

// desktopMain/kotlin/RandomJokeWindow.kt
import androidx.compose.ui.window.application
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.droidcon.notedock.model.Joke
//import com.droidcon.notedock.api.JokeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

//@Composable
//fun RandomJokeScreen(jokeApi: JokeApi) {
//    var joke by remember { mutableStateOf<Joke?>(null) }
//    var isLoading by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        if (isLoading) {
//            CircularProgressIndicator()
//        } else if (joke != null) {
//            Text(joke!!.setup, style = MaterialTheme.typography.h6)
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(joke!!.punchline, style = MaterialTheme.typography.body1)
//        } else {
//            Text("Click the button to get a random joke!")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = {
//            isLoading = true
//            CoroutineScope(Dispatchers.IO).launch {
//                joke = jokeApi.getRandomJoke()
//                isLoading = false
//            }
//        }) {
//            Text("Get Random Joke")
//        }
//    }
//}
//
//@Composable
//fun RandomJokeWindow(onClose: () -> Unit) {
//    val jokeApi = remember { JokeApi() }
//
//    Window(
//        state = rememberWindowState(width = 500.dp, height = 400.dp),
//        title = "Random Joke",
//        onCloseRequest = {
//            jokeApi.close()
//            onClose()
//        }
//    ) {
//        MaterialTheme {
//            RandomJokeScreen(jokeApi)
//        }
//    }
//}