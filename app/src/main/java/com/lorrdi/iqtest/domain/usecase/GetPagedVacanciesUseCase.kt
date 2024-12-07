package com.lorrdi.iqtest.domain.usecase

import androidx.paging.PagingData
import com.lorrdi.iqtest.data.dto.Employment
import com.lorrdi.iqtest.data.dto.Experience
import com.lorrdi.iqtest.data.dto.Schedule
import com.lorrdi.iqtest.data.dto.Vacancy
import com.lorrdi.iqtest.domain.repositories.VacancyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedVacanciesUseCase @Inject constructor(
    private val repository: VacancyRepository
) {
    operator fun invoke(
        query: String?,
        experience: List<Experience>?,
        employment: List<Employment>?,
        schedule: List<Schedule>?,
        area: List<String>?,
        orderBy: String
    ): Flow<PagingData<Vacancy>> {
        return repository.getPagedVacancies(
            query, experience, employment, schedule, area, orderBy
        )
    }
}
