package com.lorrdi.iqtest.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.lorrdi.iqtest.app.ui.composables.FilterBottomSheet
import com.lorrdi.iqtest.app.ui.composables.VacancyItem
import com.lorrdi.iqtest.app.viewmodel.VacanciesViewModel
import com.lorrdi.iqtest.data.models.FiltersResponse

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VacanciesScreen(
    viewModel: VacanciesViewModel = hiltViewModel(),
    contentPadding: PaddingValues
) {
    val vacancies = viewModel.pagedVacancies.collectAsLazyPagingItems()
    val availableCities by viewModel.availableCities.collectAsState()
    val availableFilters by viewModel.availableFilters.collectAsState()
    val availableRegions by viewModel.availableRegions.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val refreshing = vacancies.loadState.refresh is LoadState.Loading
    val refreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = {
        vacancies.refresh()
    })

    var isFilterSheetOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = "Поиск вакансий...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.updateSearchQuery(searchQuery)
                }),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    IconButton(onClick = { isFilterSheetOpen = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Открыть фильтры"
                        )
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(refreshState)
        ) {
            LazyColumn(contentPadding = contentPadding) {
                items(vacancies.itemCount) { index ->
                    vacancies[index]?.let { vacancy ->
                        VacancyItem(vacancy = vacancy)
                    }
                }

                vacancies.apply {
                    when {
                        loadState.append is LoadState.Loading -> {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .align(Alignment.TopCenter)
                                )
                            }
                        }

                        loadState.refresh is LoadState.Error -> {
                            item {
                                Text(
                                    text = "Ошибка: ${(loadState.refresh as LoadState.Error).error.message}",
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = refreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )
        }
    }

    if (isFilterSheetOpen) {
        ModalBottomSheet(onDismissRequest = { isFilterSheetOpen = false }) {
            FilterBottomSheet(
                filters = viewModel.filters.value ?: FiltersResponse(),
                availableFilters = availableFilters,
                availableRegions = availableRegions,
                onApplyFilters = { filters ->
                    viewModel.updateFilters(filters)
                    isFilterSheetOpen = false
                },
                onClose = { isFilterSheetOpen = false }
            )
        }
    }
}






