package com.mutsumonji.feature_commentviewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CommentViewerRoute(
    vm: CommentViewerViewModel = viewModel()
) {
    val comments = vm.comments.collectAsState()

    CommentViewerScreen(
        comments = comments.value,
        onAddTest = { vm.addComment("追加テスト: ${System.currentTimeMillis()}") }
    )
}