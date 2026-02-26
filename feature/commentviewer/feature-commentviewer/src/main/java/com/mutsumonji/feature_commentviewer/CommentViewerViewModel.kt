package com.mutsumonji.feature_commentviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutsumonji.core.network.WebSocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommentViewerViewModel : ViewModel() {

    private val webSocketClient = WebSocketClient()

    private val _comments = MutableStateFlow(
        listOf(
            "こんにちは！",
            "テストコメント1",
            "テストコメント2",
        )
    )
    val comments: StateFlow<List<String>> = _comments.asStateFlow()

    private var connectJob: Job? = null

    fun addComment(text: String) {
        _comments.value = _comments.value + text
    }

    fun connectWebSocket(url: String) {
        connectJob?.cancel()

        connectJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                addComment("WS: connecting $url")

                webSocketClient.connect(url).collect { message ->
                    addComment(message)
                }

                addComment("WS: flow completed")
            } catch (e: Throwable) {
                addComment("WS error: ${e.message ?: e::class.simpleName}")
            }
        }
    }

    fun disconnectWebSocket() {
        connectJob?.cancel()
        connectJob = null
        webSocketClient.disconnect()
        addComment("WS: disconnected")
    }

    fun send(text: String) {
        val ok = webSocketClient.send(text)
        addComment(if (ok) "WS: sent $text" else "WS: send failed (not connected?)")
    }

    override fun onCleared() {
        disconnectWebSocket()
        super.onCleared()
    }
}

