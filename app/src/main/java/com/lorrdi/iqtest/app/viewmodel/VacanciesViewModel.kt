package com.lorrdi.iqtest.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lorrdi.iqtest.data.models.FiltersResponse
import com.lorrdi.iqtest.data.models.Region
import com.lorrdi.iqtest.data.models.Vacancy
import com.lorrdi.iqtest.domain.usecase.GetAreasUseCase
import com.lorrdi.iqtest.domain.usecase.GetFiltersUseCase
import com.lorrdi.iqtest.domain.usecase.GetPagedVacanciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VacanciesViewModel @Inject constructor(
    private val getFiltersUseCase: GetFiltersUseCase,
    private val getAreasUseCase: GetAreasUseCase,
    private val getPagedVacanciesUseCase: GetPagedVacanciesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)
    val searchQuery: StateFlow<String?> = _searchQuery.asStateFlow()

    private val _filters = MutableStateFlow<FiltersResponse?>(null)
    val filters: StateFlow<FiltersResponse?> = _filters.asStateFlow()

    private val _sorting = MutableStateFlow("relevance")
    val sorting: StateFlow<String> = _sorting.asStateFlow()

    private val _availableRegions = MutableStateFlow<List<Region>>(emptyList())
    val availableRegions: StateFlow<List<Region>> = _availableRegions.asStateFlow()

    private val _availableFilters = MutableStateFlow<FiltersResponse?>(null)
    val availableFilters: StateFlow<FiltersResponse?> = _availableFilters.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private fun handleException(exception: Exception) {
        _errorState.value = exception.localizedMessage ?: "Неизвестная ошибка"
    }

    fun clearError() {
        _errorState.value = null
    }

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
                _availableRegions.value = getAreasUseCase()
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
        Triple(query, filters, sorting)
    }.flatMapLatest { (query, filters, sorting) ->
        getPagedVacanciesUseCase(
            query = query,
            experience = filters?.experience,
            employment = filters?.employment,
            schedule = filters?.schedule,
            area = filters?.area,
            orderBy = sorting
        )
    }.cachedIn(viewModelScope)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilters(filters: FiltersResponse) {
        _filters.value = filters
    }

    fun updateSorting(option: String) {
        _sorting.value = when (option) {
            "По релевантности" -> "relevance"
            "По дате" -> "publication_time"
            else -> "relevance"
        }
    }
}
