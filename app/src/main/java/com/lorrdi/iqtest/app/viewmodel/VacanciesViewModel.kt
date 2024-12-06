package com.lorrdi.iqtest.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lorrdi.iqtest.data.api.HhApiService
import com.lorrdi.iqtest.data.models.Area
import com.lorrdi.iqtest.data.models.City
import com.lorrdi.iqtest.data.models.FiltersResponse
import com.lorrdi.iqtest.data.models.Region
import com.lorrdi.iqtest.data.models.Vacancy
import com.lorrdi.iqtest.domain.repositories.VacancyRepository
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
    private val repository: VacancyRepository,
    private val hhApiService: HhApiService
) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)
    private val _filters = MutableStateFlow<FiltersResponse?>(null)
    private val _availableFilters = MutableStateFlow<FiltersResponse?>(null)

    val searchQuery: StateFlow<String?> = _searchQuery.asStateFlow()
    val filters: StateFlow<FiltersResponse?> = _filters.asStateFlow()
    val availableFilters: StateFlow<FiltersResponse?> = _availableFilters.asStateFlow()

    private val _availableRegions = MutableStateFlow<List<Region>>(emptyList())
    private val _availableCities =
        MutableStateFlow<List<City>>(emptyList()) // Новое состояние для городов

    val availableRegions: StateFlow<List<Region>> = _availableRegions.asStateFlow()
    val availableCities: StateFlow<List<City>> =
        _availableCities.asStateFlow() // Получаем доступ к городам


    init {
        getAvailableFilters()
        getAvailableRegions()
    }

    private fun getAvailableRegions() {
        viewModelScope.launch {
            try {
                val response = hhApiService.getAreas() // Get region data
                _availableRegions.value = response // Assign to the state variable

                // Flattening regions and areas to get cities
                val cities = response.flatMap { region ->
                    region.areas.flatMap { area ->
                        area.areas?.map { city ->
                            City(id = city.id, name = city.name) // Mapping the city to City object
                        } ?: emptyList()
                    }
                }
                _availableCities.value = cities // Update cities state
            } catch (e: Exception) {
                Log.e("VacanciesViewModel", "Error fetching regions", e)
            }
        }
    }

    private fun getAvailableFilters() {
        viewModelScope.launch {
            try {
                val response = hhApiService.getFilters()
                Log.d("DEBUG_F", response.toString())
                _availableFilters.value = response
            } catch (e: Exception) {
                throw e
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedVacancies: Flow<PagingData<Vacancy>> = combine(
        searchQuery,
        filters,
        availableFilters,
        availableRegions
    ) { query, filters, availableFilters, _ ->
        Triple(query, filters, availableFilters)
    }.flatMapLatest { (query, filters) ->
        repository.getPagedVacancies(
            query = query,
            experience = filters?.experience,
            employment = filters?.employment,
            schedule = filters?.schedule,
            area = filters?.area,
        )
    }.cachedIn(viewModelScope)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilters(filters: FiltersResponse) {
        _filters.value = filters
    }
}

