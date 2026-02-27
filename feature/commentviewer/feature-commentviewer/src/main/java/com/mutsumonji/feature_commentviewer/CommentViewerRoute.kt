package com.mutsumonji.feature_commentviewer

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CommentViewerRoute(
    vm: CommentViewerViewModel = viewModel()
) {
    val context = LocalContext.current
    val comments by vm.comments.collectAsState()
    val wsState by vm.wsState.collectAsState()

    var url by remember { mutableStateOf("ws://10.0.2.2:8080") } // ローカルWS安定
    var sendText by remember { mutableStateOf("") }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) vm.importTextFile(context, uri)
    }

    CommentViewerScreen(
        wsState = wsState,
        url = url,
        onUrlChange = { url = it },
        sendText = sendText,
        onSendTextChange = { sendText = it },
        comments = comments,
        onConnect = { vm.connectWebSocket(url) },
        onDisconnect = { vm.disconnectWebSocket() },
        onSend = {
            vm.send(sendText)
            sendText = ""
        },
        onAddTest = { vm.addComment("追加テスト: ${System.currentTimeMillis()}") },
        onImportTxt = { picker.launch(arrayOf("text/plain")) },
    )
}