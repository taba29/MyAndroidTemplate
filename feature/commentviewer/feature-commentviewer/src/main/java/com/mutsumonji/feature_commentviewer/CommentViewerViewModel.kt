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

    // ✅ 接続状態
    private val _wsState = MutableStateFlow(WsState.Disconnected)
    val wsState: StateFlow<WsState> = _wsState.asStateFlow()

    private var connectJob: Job? = null

    fun addComment(text: String) {
        _comments.value = _comments.value + text
    }

    fun connectWebSocket(url: String) {
        // 二重接続防止（前の接続を止めてから繋ぎ直す）
        connectJob?.cancel()
        webSocketClient.disconnect()

        _wsState.value = WsState.Connecting

        connectJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                addComment("WS: connecting $url")

                webSocketClient.connect(url).collect { message ->
                    // ※ 受信が来たら Connected 扱い（簡易）
                    if (_wsState.value != WsState.Connected) {
                        _wsState.value = WsState.Connected
                        addComment("WS: connected")
                    }
                    addComment(message)
                }

                // flow が自然終了したら切断扱い
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