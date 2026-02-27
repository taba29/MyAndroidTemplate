package com.mutsumonji.feature_commentviewer

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutsumonji.core.network.WebSocketClient
import com.mutsumonji.core_storage.TextFileReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommentViewerViewModel : ViewModel() {

    private val webSocketClient = WebSocketClient()
    private val textFileReader = TextFileReader()

    private val _comments = MutableStateFlow(
        listOf("こんにちは！", "テストコメント1", "テストコメント2")
    )
    val comments: StateFlow<List<String>> = _comments.asStateFlow()

    private val _wsState = MutableStateFlow(WsState.Disconnected)
    val wsState: StateFlow<WsState> = _wsState.asStateFlow()

    private var connectJob: Job? = null

    fun addComment(text: String) {
        _comments.update { it + text }
    }

    fun importTextFile(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val text = textFileReader.readText(context, uri)

                val lines = text.lines()
                    .map { it.trim() }
                    .filter { it.isNotBlank() }

                if (lines.isEmpty()) {
                    addComment("[FILE] empty")
                    return@launch
                }

                _comments.update { it + lines.map { line -> "[FILE] $line" } }
            } catch (e: Throwable) {
                addComment("[FILE] error: ${e.message ?: e::class.simpleName}")
            }
        }
    }

    fun connectWebSocket(url: String) {
        connectJob?.cancel()
        webSocketClient.disconnect()

        _wsState.value = WsState.Connecting

        connectJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                addComment("WS: connecting $url")

                webSocketClient.connect(url).collect { message ->
                    if (_wsState.value != WsState.Connected) {
                        _wsState.value = WsState.Connected
                        addComment("WS: connected")
                    }
                    addComment(message)
                }

                _wsState.value = WsState.Disconnected
                addComment("WS: flow completed")
            } catch (e: Throwable) {
                _wsState.value = WsState.Error
                addComment("WS error: ${e.message ?: e::class.simpleName}")
            }
        }
    }

    fun disconnectWebSocket() {
        connectJob?.cancel()
        connectJob = null
        webSocketClient.disconnect()
        _wsState.value = WsState.Disconnected
        addComment("WS: disconnected")
    }

    fun send(text: String) {
        val t = text.trim()
        if (t.isEmpty()) return

        val ok = webSocketClient.send(t)
        addComment(if (ok) "WS: sent $t" else "WS: send failed (not connected?)")
    }

    override fun onCleared() {
        disconnectWebSocket()
        super.onCleared()
    }
}