package com.mutsumonji.feature_commentviewer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CommentViewerViewModel : ViewModel() {

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
}