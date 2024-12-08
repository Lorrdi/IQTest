package com.lorrdi.iqtest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lorrdi.iqtest.data.dto.ErrorState
import com.lorrdi.iqtest.data.dto.Filters
import com.lorrdi.iqtest.data.dto.Region
import com.lorrdi.iqtest.data.dto.Vacancy
import com.lorrdi.iqtest.domain.entities.VacancySearchParams
import com.lorrdi.iqtest.domain.enums.SortingOption
import com.lorrdi.iqtest.domain.usecase.GetFiltersUseCase
import com.lorrdi.iqtest.domain.usecase.GetPagedVacanciesUseCase
import com.lorrdi.iqtest.domain.usecase.GetRegionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class VacanciesViewModel @Inject constructor(
    private val getFiltersUseCase: GetFiltersUseCase,
    private val getRegionsUseCase: GetRegionsUseCase,
    private val getPagedVacanciesUseCase: GetPagedVacanciesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)
    val searchQuery: StateFlow<String?> = _searchQuery.asStateFlow()

    private val _filters = MutableStateFlow<Filters?>(null)
    val filters: StateFlow<Filters?> = _filters.asStateFlow()

    private val _sorting = MutableStateFlow(SortingOption.RELEVANCE)
    val sorting: StateFlow<SortingOption> = _sorting.asStateFlow()

    private val _availableRegions = MutableStateFlow<List<Region>>(emptyList())
    val availableRegions: StateFlow<List<Region>> = _availableRegions.asStateFlow()

    private val _availableFilters = MutableStateFlow<Filters?>(null)
    val availableFilters: StateFlow<Filters?> = _availableFilters.asStateFlow()

    private val _errorState = MutableStateFlow<ErrorState?>(null)
    val errorState: StateFlow<ErrorState?> = _errorState.asStateFlow()

    fun fetchAvailableFilters() {
        viewModelScope.launch {
            try {
                _availableFilters.value = getFiltersUseCase()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun fetchAvailableRegions() {
        viewModelScope.launch {
            try {
                _availableRegions.value = getRegionsUseCase()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedVacancies: Flow<PagingData<Vacancy>> = combine(
        searchQuery,
        filters,
        sorting
    ) { query, filters, sorting ->
        VacancySearchParams(query, filters, sorting)
    }.flatMapLatest { params ->
        getPagedVacanciesUseCase(
            params = params
        )
    }.cachedIn(viewModelScope)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilters(filters: Filters) {
        _filters.value = filters
    }

    fun updateSorting(option: SortingOption) {
        _sorting.value = option
    }

    private fun handleException(exception: Exception): ErrorState {
        return when (exception) {
            is IOException -> ErrorState.NetworkError
            is HttpException -> ErrorState.ServerError(exception.code())
            else -> ErrorState.UnknownError(exception.message ?: "Unknown error")
        }
    }

    fun clearError() {
        _errorState.value = null
    }
}

