package com.droidcon.notedock.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.notedock.model.Joke
import com.droidcon.notedock.model.JokeResult
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val JOKE_API_URL = "https://official-joke-api.appspot.com/random_joke"

@Composable
fun JokeScreen(modifier: Modifier = Modifier, httpClient: HttpClient) {
    val scope = rememberCoroutineScope()
    var jokeState: JokeResult by remember { mutableStateOf(JokeResult.NotStarted) }


    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        snackbarHost = { snackbarHostState }
    ) {

        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Button({
                //Start loading state
                jokeState = JokeResult.Loading

                //Launch in the IO dispatcher for network operations
                scope.launch(Dispatchers.IO) {
                    try {
                        val response: HttpResponse = httpClient.get(JOKE_API_URL)
                        if (response.status.value == 200) {
                            val joke = response.body<Joke>()
                            withContext(Dispatchers.Main) {
                                jokeState = JokeResult.Success(joke)
                            }
                        } else {
                            jokeState = JokeResult.Error("Failed to get joke from API: ${response.status.value}")
                        }

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            jokeState = JokeResult.Error("Error during call to API: ${e.message ?: "Unknown error"}")
                        }
                    }

                }
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Get a random joke")
            }

            when (jokeState) {
                is JokeResult.NotStarted -> {
                    // Do nothing
                }

                is JokeResult.Loading -> {
                    Text("Loading joke...", Modifier.padding(8.dp))
                    CircularProgressIndicator(modifier = Modifier.padding(8.dp).fillMaxSize(0.5f))
                }

                is JokeResult.Success -> {
                    val joke = (jokeState as JokeResult.Success).joke
                    Text("Setup: ${joke.setup}", modifier = Modifier.padding(8.dp))
                    Text("Punchline: ${joke.punchline}", modifier = Modifier.padding(8.dp))
                }

                is JokeResult.Error -> {
                    val error = (jokeState as JokeResult.Error).message
                    Text(
                        "An error occurred: $error",
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

        }
    }

}