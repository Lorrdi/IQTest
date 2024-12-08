package com.lorrdi.iqtest.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.lorrdi.iqtest.data.dto.Filters
import com.lorrdi.iqtest.domain.enums.SortingOption
import com.lorrdi.iqtest.presentation.ui.components.ErrorDialog
import com.lorrdi.iqtest.presentation.ui.components.SearchBar
import com.lorrdi.iqtest.presentation.ui.components.VacancyList
import com.lorrdi.iqtest.presentation.ui.components.bottomsheets.FilterBottomSheet
import com.lorrdi.iqtest.presentation.viewmodel.VacanciesViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VacanciesScreen(
    viewModel: VacanciesViewModel = hiltViewModel(),
    contentPadding: PaddingValues
) {
    val vacancies = viewModel.pagedVacancies.collectAsLazyPagingItems()
    val refreshing = vacancies.loadState.refresh is LoadState.Loading
    val refreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = {
        vacancies.refresh()
    })
    var isSortingMenuExpanded by remember { mutableStateOf(false) }
    var isFilterSheetOpen by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedSorting by remember { mutableStateOf(SortingOption.RELEVANCE) }
    val errorState by viewModel.errorState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchAvailableFilters()
        viewModel.fetchAvailableRegions()
    }

    if (showErrorDialog && errorState != null) {
        ErrorDialog(
            errorState = errorState,
            onRetry = {
                viewModel.clearError()
                showErrorDialog = false
                vacancies.refresh()
            },
            onDismiss = { showErrorDialog = false }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onSearch = { viewModel.updateSearchQuery(searchQuery) },
            onFilterClick = { isFilterSheetOpen = true },
            onSortingOptionSelected = { option ->
                selectedSorting = option
                isSortingMenuExpanded = false
                viewModel.updateSorting(option)
            }
        )

        VacancyList(
            vacancies = vacancies,
            contentPadding = contentPadding,
            refreshState = refreshState,
            refreshing = refreshing
        )
    }

    if (isFilterSheetOpen) {
        FilterBottomSheet(
            filters = viewModel.filters.value ?: Filters(),
            availableFilters = viewModel.availableFilters.collectAsState().value
                ?: Filters(),
            availableRegions = viewModel.availableRegions.collectAsState().value,
            onApplyFilters = { filters -> viewModel.updateFilters(filters) },
            onClose = { isFilterSheetOpen = false }
        )
    }
}

