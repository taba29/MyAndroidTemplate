package com.mutsumonji.feature_commentviewer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CommentViewerScreen(
    comments: List<String>,
    onAddTest: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Comment Viewer")

        Button(
            onClick = onAddTest,
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
        ) {
            Text("Add test comment")
        }

        LazyColumn {
            items(comments) { c ->
                Text("• $c")
            }
        }
    }
}