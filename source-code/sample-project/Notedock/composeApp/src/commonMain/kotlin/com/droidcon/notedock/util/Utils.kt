package com.droidcon.notedock.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Specifies a Windows's status i.e. open or exit
 */
enum class WinStatus {
    Open,
    Exit
}

enum class AppWindow{
    EditorWindow,
    QuickNoteWindow,
    JokeWindow
}


fun convertTimestampToDateString(timestampMillis: Long, pattern: String): String {
    val date = Date(timestampMillis)
    val formatter = SimpleDateFormat(pattern, Locale.getDefault()) // Or specify a desired locale
    return formatter.format(date)
}
