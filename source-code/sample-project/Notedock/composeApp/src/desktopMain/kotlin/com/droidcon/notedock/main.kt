package com.droidcon.notedock


import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import androidx.compose.ui.window.rememberWindowState
import com.droidcon.notedock.ui.screens.MainNotesWindow
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.engine.cio.CIO
import org.jetbrains.compose.resources.painterResource
import notedock.composeapp.generated.resources.Res
import notedock.composeapp.generated.resources.notedock_icon

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


// Define shared HttpClient instance at the top level
val appHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }

    //Remove engine proxy block below if not needed
    engine {
        proxy = ProxyBuilder.http(Url("http://127.0.0.1:9910"))
    }

}

fun main() = application {
    val windowState = rememberWindowState()
    val trayState = rememberTrayState()

    DisposableEffect(Unit) { // 'Unit' key means this effect runs once when the app starts
        onDispose {
            println("Application shutting down. Closing HttpClient.")
            appHttpClient.close() // Close the HttpClient when the application is disposed
        }
    }


    /**
     * Controls opening of Quick Note Window
     */
    var isQuickNoteWindowOpen by remember { mutableStateOf(false) }

    MainNotesWindow(
        onCloseQuickNote = { isQuickNoteWindowOpen = false },
        isQuickNoteWindowOpen = isQuickNoteWindowOpen,
        windowState = windowState,
        onCloseApp = ::exitApplication,
        httpClient = appHttpClient
    )

    Tray(
        state = trayState,
        icon = painterResource(Res.drawable.notedock_icon), // We'll add this resource later
        menu = {
            Item("New Note") {
                isQuickNoteWindowOpen = true
            }
            Item("Sync") {
                trayState.sendNotification(
                    Notification(
                        title = "Sync Status",
                        message = "Sync successful!",
                        type = Notification.Type.Info
                    )
                )
                // In a real app, you'd trigger your sync logic here
            }
            Separator()
            Item("Quit") {
                exitApplication()
            }
        }
    )

//    }
}