package com.droidcon.notedock.model

data class Note (val id: Int, val title: String, val content: String, val timestamp: Long = System.currentTimeMillis())

