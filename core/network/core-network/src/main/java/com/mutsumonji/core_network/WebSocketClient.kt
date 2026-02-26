package com.mutsumonji.core.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*

class WebSocketClient {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect(url: String): Flow<String> = callbackFlow {

        val request = Request.Builder()
            .url(url)
            .build()

        val listener = object : WebSocketListener() {

            override fun onOpen(ws: WebSocket, response: Response) {
                webSocket = ws
            }

            override fun onMessage(ws: WebSocket, text: String) {
                trySend(text)
            }

            override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                ws.close(1000, null)
                webSocket = null
                close()
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                webSocket = null
                close(t)
            }
        }

        webSocket = client.newWebSocket(request, listener)

        awaitClose {
            webSocket?.close(1000, null)
            webSocket = null
        }
    }

    fun disconnect() {
        webSocket?.close(1000, null)
        webSocket = null
    }
}