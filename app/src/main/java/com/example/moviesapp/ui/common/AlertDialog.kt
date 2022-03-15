package com.example.moviesapp.ui.common

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.material.TextButton

@Composable
fun SimpleAlertDialog(
    description: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm)
            { Text(text = "OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss)
            { Text(text = "Cancel") }
        },
        title = { Text(text = "Warning alert") },
        text = { Text(text = description) }
    )
}