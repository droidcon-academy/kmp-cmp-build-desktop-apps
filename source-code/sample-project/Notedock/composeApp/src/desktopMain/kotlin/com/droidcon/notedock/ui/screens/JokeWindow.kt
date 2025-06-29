package com.droidcon.notedock.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import com.droidcon.notedock.api.JokeApi
import com.droidcon.notedock.ui.components.JokeScreen

@Composable
fun JokeWindow(
    title: String,
    onCloseRequest: () -> Unit
){
    Window(title = title, onCloseRequest = onCloseRequest){
        JokeScreen(jokeApi = remember { JokeApi() })
    }
}