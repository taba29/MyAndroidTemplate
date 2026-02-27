package com.mutsumonji.core_storage

import android.content.Context
import android.net.Uri

class TextFileReader {
    fun readText(context: Context, uri: Uri): String {
        return context.contentResolver.openInputStream(uri)?.use { input ->
            input.bufferedReader().use { it.readText() }
        } ?: ""
    }
}