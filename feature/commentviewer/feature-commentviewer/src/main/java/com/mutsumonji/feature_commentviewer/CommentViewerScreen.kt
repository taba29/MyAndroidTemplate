package com.mutsumonji.feature_commentviewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CommentViewerScreen(
    wsState: WsState,

    url: String,
    onUrlChange: (String) -> Unit,

    sendText: String,
    onSendTextChange: (String) -> Unit,

    comments: List<String>,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onSend: () -> Unit,
    onAddTest: () -> Unit,
    onImportTxt: () -> Unit,
) {
    val canSend = (wsState == WsState.Connected) && sendText.isNotBlank()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Comment Viewer")
        Spacer(Modifier.height(8.dp))
        Text("Status: $wsState")

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = url,
            onValueChange = onUrlChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("WebSocket URL") },
            placeholder = { Text("ws://10.0.2.2:8080") }
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onConnect,
                modifier = Modifier.weight(1f),
                enabled = wsState != WsState.Connecting
            ) { Text("Connect") }

            OutlinedButton(
                onClick = onDisconnect,
                modifier = Modifier.weight(1f),
                enabled = wsState != WsState.Disconnected
            ) { Text("Disconnect") }
        }

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = sendText,
            onValueChange = onSendTextChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Send text") },
            placeholder = { Text("hello") }
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onSend,
            modifier = Modifier.fillMaxWidth(),
            enabled = canSend
        ) { Text("Send") }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onAddTest,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Add test comment") }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onImportTxt,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Import TXT") }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(comments) { c ->
                Text("• $c")
                Spacer(Modifier.height(6.dp))
            }
        }
    }
}