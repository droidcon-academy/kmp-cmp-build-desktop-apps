package com.droidcon.notedock.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
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