package com.lorrdi.iqtest.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.lorrdi.iqtest.data.dto.Filters
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

    val searchQuery by viewModel.searchQuery.collectAsState()
    val filters by viewModel.filters.collectAsState()
    val availableFilters by viewModel.availableFilters.collectAsState()
    val availableRegions by viewModel.availableRegions.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    if (errorState != null) {
        ErrorDialog(
            errorState = errorState,
            onRetry = {
                viewModel.clearError()
                vacancies.refresh()
            },
            onDismiss = { viewModel.clearError() }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            searchQuery = searchQuery ?: "",
            onSearchQueryChange = { viewModel.updateSearchQuery(it) },
            onSearch = { viewModel.updateSearchQuery(searchQuery ?: "") },
            onFilterClick = { viewModel.openFilterSheet() },
            onSortingOptionSelected = { viewModel.updateSorting(it) }
        )
        VacancyList(
            vacancies = vacancies,
            contentPadding = contentPadding,
            refreshState = refreshState,
            refreshing = refreshing
        )
    }

    if (viewModel.isFilterSheetOpen.collectAsState().value) {
        FilterBottomSheet(
            filters = filters ?: Filters(),
            availableFilters = availableFilters,
            availableRegions = availableRegions,
            onApplyFilters = { viewModel.updateFilters(it) },
            onClose = { viewModel.closeFilterSheet() }
        )
    }
}


