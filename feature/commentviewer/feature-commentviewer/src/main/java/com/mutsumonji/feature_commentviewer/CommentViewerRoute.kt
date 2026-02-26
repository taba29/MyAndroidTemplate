package com.mutsumonji.feature_commentviewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CommentViewerRoute(
    vm: CommentViewerViewModel = viewModel()
) {
    val comments by vm.comments.collectAsState()

    var url by rememberSaveable { mutableStateOf("wss://echo.websocket.org") }
    var sendText by rememberSaveable { mutableStateOf("") }

    CommentViewerScreen(
        url = url,
        onUrlChange = { url = it },

        sendText = sendText,
        onSendTextChange = { sendText = it },

        comments = comments,
        onConnect = { vm.connectWebSocket(url) },
        onDisconnect = { vm.disconnectWebSocket() },
        onSend = {
            vm.send(sendText)
            sendText = "" // 送ったらクリア（好みで消してOK）
        },
        onAddTest = { vm.addComment("追加テスト: ${System.currentTimeMillis()}") }
    )
}