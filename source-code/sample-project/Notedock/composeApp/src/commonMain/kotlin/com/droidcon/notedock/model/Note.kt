package com.droidcon.notedock.model

import java.util.UUID
import kotlin.uuid.Uuid

data class Note (val id: Int, val title: String, val content: String, val timestamp: Long = System.currentTimeMillis())

