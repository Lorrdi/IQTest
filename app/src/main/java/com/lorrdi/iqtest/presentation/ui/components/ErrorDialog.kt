package com.lorrdi.iqtest.presentation.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.lorrdi.iqtest.data.dto.ErrorState

@Composable
fun ErrorDialog(
    errorState: ErrorState?,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text("Повторить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        text = {
            Text(
                text = when (errorState) {
                    is ErrorState.NetworkError -> "Проблема с сетью. Проверьте подключение и попробуйте снова."
                    is ErrorState.ServerError -> "Ошибка сервера: код ${errorState.code}. Попробуйте позже."
                    is ErrorState.UnknownError -> errorState.message
                    else -> "Неизвестная ошибка."
                }
            )
        },
        title = {
            Text(text = "Ошибка", style = MaterialTheme.typography.titleMedium)
        }
    )
}
