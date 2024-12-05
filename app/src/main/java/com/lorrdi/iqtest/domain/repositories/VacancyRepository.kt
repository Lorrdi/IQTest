package com.lorrdi.iqtest.domain.repositories

import androidx.paging.PagingData
import com.lorrdi.iqtest.data.models.VacanciesResponse
import com.lorrdi.iqtest.data.models.Vacancy
import kotlinx.coroutines.flow.Flow

interface VacancyRepository {
    fun getPagedVacancies(
        query: String? = null,
        experience: String? = null,
        employment: String? = null,
        schedule: String? = null,
        area: String? = null
    ): Flow<PagingData<Vacancy>>
}