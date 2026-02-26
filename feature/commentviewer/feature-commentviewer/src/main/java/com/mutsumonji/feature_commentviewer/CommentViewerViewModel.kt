package com.mutsumonji.feature_commentviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutsumonji.core.network.WebSocketClient
import kotlinx.coroutines.Dispatchers
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

    fun addComment(text: String) {
        _comments.value = _comments.value + text
    }

    fun connectWebSocket(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                webSocketClient.connect(url).collect { message ->
                    addComment(message)
                }
            } catch (e: Throwable) {
                addComment("WS error: ${e.message ?: e::class.simpleName}")
            }
        }
    }

    override fun onCleared() {
        webSocketClient.disconnect()
        super.onCleared()
    }
}