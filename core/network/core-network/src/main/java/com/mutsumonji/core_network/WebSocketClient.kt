package com.mutsumonji.core.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

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

    /** 接続中なら送れる。未接続なら false */
    fun send(text: String): Boolean {
        val ws = webSocket ?: return false
        return ws.send(text)
    }

    fun disconnect() {
        webSocket?.close(1000, null)
        webSocket = null
    }
}