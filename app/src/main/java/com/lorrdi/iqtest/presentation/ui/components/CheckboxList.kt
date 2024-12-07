package com.lorrdi.iqtest.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun CheckboxList(
    label: String,
    selectedItems: List<String>,
    items: List<String>,
    onItemSelectionChanged: (String, Boolean) -> Unit
) {
    Text(label)
    Column {
        items.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectedItems.contains(item),
                    onCheckedChange = { isChecked ->
                        onItemSelectionChanged(item, isChecked)
                    }
                )
                Text(item)
            }
        }
    }
}