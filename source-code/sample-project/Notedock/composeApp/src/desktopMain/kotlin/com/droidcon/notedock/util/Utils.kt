package com.droidcon.notedock.util

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import org.jetbrains.skiko.hostOs

class Utils {
    companion object {
        fun handleKeyEvent(event: KeyEvent) : Boolean {
            when (event.key) {
                Key.C -> {
                    if (event.isCtrlPressed) {
                        print("Ctrl + C Pressed\n")
                        true
                    }
                    false
                }

                Key.A -> {
                    if (event.isCtrlPressed) {
                        print("Ctrl + A Pressed")
                        true
                    }
                }

                else -> false
            }

            return false
        }
    }
}