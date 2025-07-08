package com.droidcon.notedock.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
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
import com.droidcon.notedock.api.JokeApi
import com.droidcon.notedock.model.Joke
import kotlinx.coroutines.launch

@Composable
fun JokeScreen(modifier: Modifier = Modifier, jokeApi: JokeApi){
    val scope = rememberCoroutineScope ()
    var isLoading by remember { mutableStateOf(false) }
    var joke by remember { mutableStateOf<Joke?>(null)}

    Column (modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Button({
            scope.launch {
                isLoading = true
                joke = jokeApi.getRandomJoke()
                isLoading = false
             }
        }, modifier = Modifier.align(Alignment.CenterHorizontally)){
            Text("Get a random joke")
        }

        if (isLoading){
            CircularProgressIndicator()
        }

        else if (joke != null){
            Text(joke!!.setup, Modifier.padding(8.dp), style = MaterialTheme.typography.titleSmall)
            Text(joke!!.punchline, Modifier.padding(8.dp), style = MaterialTheme.typography.bodyLarge)
        }

    }


}