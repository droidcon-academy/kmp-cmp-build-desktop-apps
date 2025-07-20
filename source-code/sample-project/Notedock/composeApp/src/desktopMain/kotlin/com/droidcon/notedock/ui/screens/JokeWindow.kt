package com.droidcon.notedock.ui.screens

 import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import com.droidcon.notedock.ui.components.JokeScreen

@Composable
fun JokeWindow(
    title: String,
    onCloseRequest: () -> Unit
){
    Window(title = title, onCloseRequest = onCloseRequest){
        JokeScreen(modifier = Modifier.fillMaxSize().padding(8.dp))
    }
}