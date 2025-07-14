package com.droidcon.notedock.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.notedock.model.Joke
import com.droidcon.notedock.model.JokeResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

const val JOKE_API_URL = "https://official-joke-api.appspot.com/random_joke"
@Composable
fun JokeScreen(modifier: Modifier = Modifier){
    val scope = rememberCoroutineScope ()
    var jokeState: JokeResult by remember{ mutableStateOf(JokeResult.NotStarted)}
    val httpClient = HttpClient()

    Column (modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Button({
            //Start loading state
            jokeState = JokeResult.Loading

            //Launch in the IO dispatcher for network operations
            scope.launch(Dispatchers.IO) {
                try{
                    val response : HttpResponse = httpClient.get(JOKE_API_URL)
                    if (response.status.value == 200){
                        val joke = response.body<Joke>()
                        withContext(Dispatchers.Main){
                            jokeState = JokeResult.Success(joke)
                        }
                    }
                    else {
                        jokeState = JokeResult.Error("Failed to get joke from API: ${response.status.value}")
                    }

                } catch(e: Exception){
                    withContext(Dispatchers.Main) {
                        jokeState = JokeResult.Error("Error during call to API: ${e.message ?: "Unknown error"}")
                    }
                }

             }
        }, modifier = Modifier.align(Alignment.CenterHorizontally)){
            Text("Get a random joke")
        }

        when (jokeState){
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
                Text("An error occurred: $error", modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.error)
            }
        }

    }


}