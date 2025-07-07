package com.droidcon.notedock



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
import org.jetbrains.compose.resources.painterResource
import notedock.composeapp.generated.resources.Res
import notedock.composeapp.generated.resources.notedock_icon


fun main() = application {
    val windowState = rememberWindowState()
    val trayState = rememberTrayState()
    /**
     * Controls opening of Quick Note Window
     */
    var isQuickNoteWindowOpen by remember{ mutableStateOf(false) }

    MainNotesWindow(
        onCloseQuickNote = { isQuickNoteWindowOpen = false},
        isQuickNoteWindowOpen = isQuickNoteWindowOpen,
        windowState = windowState,
        onCloseApp = ::exitApplication,
        onShowNotification = {
            trayState.sendNotification(
                Notification(it.title, it.message, it.type)
            )
        }
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