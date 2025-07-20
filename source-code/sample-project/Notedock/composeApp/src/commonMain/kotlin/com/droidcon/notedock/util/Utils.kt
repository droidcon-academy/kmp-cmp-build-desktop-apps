package com.droidcon.notedock.util

import androidx.compose.ui.input.key.*
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import java.text.SimpleDateFormat
import java.util.*


fun convertTimestampToDateString(timestampMillis: Long, pattern: String): String {
    val date = Date(timestampMillis)
    val formatter = SimpleDateFormat(pattern, Locale.getDefault()) // Or specify a desired locale
    return formatter.format(date)
}



class Utils {
    companion object {
        /**
         * Generates random sentences
         */
        fun generateRandomSentence(): String{
            val subjects = listOf(
                "We",
                "Police",
                "People",
                "Developers",
                "Countries"
            )

            val objects = listOf(
                "buildings",
                "books",
                "computers",
                "AI",
                "technologies",
                "Jetpack Compose"
            )

            val verbs = listOf(
                "love",
                "hate",
                "destroy",
                "program",
                "fix",
                "make"
            )

            val adverbs = listOf(
                "foolishly",
                "intelligently",
                "quickly",
                "lazily",
                "slowly"
            )

            val adjectives = listOf(
                "beautiful",
                "big",
                "small",
                "lovely",
                "ridiculous"
            )

            return subjects.random() + " " + adverbs.random() + " " + verbs.random() + " " + adjectives.random() + " " + objects.random()
        }
    }

}


/**
 * Handles keyboard shortcuts for the main notes window
 */
fun handleMainWindowKbShortcuts(
    event: KeyEvent,
    onOpenNewNoteWindow: () -> Unit,
    onOpenJokeWindow: () -> Unit
): Boolean {
    if (event.type == KeyEventType.KeyDown) {
        println("Key event: ${event.key}")
        // Handle Cmd and Ctrl based on platform
        val isPrimaryModifierPressed = if (hostOs == OS.MacOS) event.isMetaPressed else event.isCtrlPressed

        val consumedByMe = if (isPrimaryModifierPressed && event.key == Key.N) {
            onOpenNewNoteWindow()
            true
        } else if (isPrimaryModifierPressed && event.isShiftPressed && event.key == Key.D) {
            onOpenJokeWindow()
            true
        } else false
        return consumedByMe
    }
    return false

}

