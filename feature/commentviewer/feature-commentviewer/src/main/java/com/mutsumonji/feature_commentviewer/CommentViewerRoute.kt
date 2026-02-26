package com.mutsumonji.feature_commentviewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CommentViewerRoute(
    vm: CommentViewerViewModel = viewModel()
) {
    val comments by vm.comments.collectAsState()

    // 🔌 画面表示時に1回だけWS接続

    LaunchedEffect(Unit) {
        vm.connectWebSocket("wss://echo.websocket.org")
        // もしくは
        // vm.connectWebSocket("wss://echo-websocket.fly.dev")
    }

    CommentViewerScreen(
        comments = comments,
        onAddTest = { vm.addComment("追加テスト: ${System.currentTimeMillis()}") }
    )
}