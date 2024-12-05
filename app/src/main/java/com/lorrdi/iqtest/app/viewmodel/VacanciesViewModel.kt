package com.lorrdi.iqtest.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lorrdi.iqtest.data.models.Vacancy
import com.lorrdi.iqtest.domain.repositories.VacancyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class VacanciesViewModel @Inject constructor(
    repository: VacancyRepository
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val pagedVacancies: Flow<PagingData<Vacancy>> = repository.getPagedVacancies()
        .cachedIn(viewModelScope)

}

