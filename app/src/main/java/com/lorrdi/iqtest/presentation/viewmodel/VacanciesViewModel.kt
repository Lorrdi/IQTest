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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
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
    private val _filters = MutableStateFlow<Filters?>(null)
    private val _sorting = MutableStateFlow(SortingOption.RELEVANCE)
    private val _isFilterSheetOpen = MutableStateFlow(false)

    val searchQuery: StateFlow<String?> = _searchQuery.asStateFlow()
    val filters: StateFlow<Filters?> = _filters.asStateFlow()
    val sorting: StateFlow<SortingOption> = _sorting.asStateFlow()
    val isFilterSheetOpen: StateFlow<Boolean> = _isFilterSheetOpen.asStateFlow()

    val availableRegions: StateFlow<List<Region>> =
        fetchWithStateFlow(emptyList()) { getRegionsUseCase() }
    val availableFilters: StateFlow<Filters> = fetchWithStateFlow(Filters()) { getFiltersUseCase() }

    private val _errorState = MutableStateFlow<ErrorState?>(null)
    val errorState: StateFlow<ErrorState?> = _errorState.asStateFlow()

    private val vacancyParams = combine(
        _searchQuery,
        _filters,
        _sorting
    ) { query, filters, sorting ->
        VacancySearchParams(
            query = query ?: "",
            filters = filters ?: Filters(),
            sorting = sorting
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        VacancySearchParams("", Filters(), sorting.value)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedVacancies: Flow<PagingData<Vacancy>> = vacancyParams
        .flatMapLatest { params ->
            getPagedVacanciesUseCase(params)
        }
        .cachedIn(viewModelScope)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilters(filters: Filters) {
        _filters.value = filters
    }

    fun updateSorting(option: SortingOption) {
        _sorting.value = option
    }

    fun clearError() {
        _errorState.value = null
    }

    fun openFilterSheet() {
        _isFilterSheetOpen.value = true
    }

    fun closeFilterSheet() {
        _isFilterSheetOpen.value = false
    }

    private fun <T> fetchWithStateFlow(
        defaultValue: T,
        block: suspend () -> T
    ): StateFlow<T> {
        val stateFlow = MutableStateFlow(defaultValue)
        viewModelScope.launch {
            try {
                stateFlow.value = block()
            } catch (e: Exception) {
                _errorState.value = handleException(e)
            }
        }
        return stateFlow.asStateFlow()
    }

    private fun handleException(exception: Exception): ErrorState {
        return when (exception) {
            is IOException -> ErrorState.NetworkError
            is HttpException -> ErrorState.ServerError(exception.code())
            else -> ErrorState.UnknownError(exception.message ?: "Unknown error")
        }
    }
}


