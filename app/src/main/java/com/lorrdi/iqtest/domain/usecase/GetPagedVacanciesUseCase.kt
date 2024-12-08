package com.lorrdi.iqtest.domain.usecase

import androidx.paging.PagingData
import com.lorrdi.iqtest.data.dto.Vacancy
import com.lorrdi.iqtest.domain.entities.VacancySearchParams
import com.lorrdi.iqtest.domain.repositories.VacancyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedVacanciesUseCase @Inject constructor(
    private val repository: VacancyRepository
) {
    operator fun invoke(params: VacancySearchParams): Flow<PagingData<Vacancy>> {
        return repository.getPagedVacancies(params)
    }
}