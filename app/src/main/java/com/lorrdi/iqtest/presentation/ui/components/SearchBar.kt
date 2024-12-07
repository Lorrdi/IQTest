package com.lorrdi.iqtest.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.lorrdi.iqtest.R
import com.lorrdi.iqtest.domain.enums.SortingOption

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onFilterClick: () -> Unit,
    onSortClick: () -> Unit,
    isSortingMenuExpanded: Boolean,
    onSortingOptionSelected: (SortingOption) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = "Поиск вакансий...") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                Row {
                    IconButton(onClick = onFilterClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter),
                            contentDescription = "Открыть фильтры"
                        )
                    }
                    IconButton(onClick = onSortClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_sort),
                            contentDescription = "Сортировка"
                        )
                    }
                    DropdownMenu(
                        expanded = isSortingMenuExpanded,
                        onDismissRequest = { onSortClick() }
                    ) {
                        SortingOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.displayName) },
                                onClick = { onSortingOptionSelected(option) }
                            )
                        }
                    }
                }
            }
        )
    }
}