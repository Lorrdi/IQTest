package com.lorrdi.iqtest.domain.repositories

import androidx.paging.PagingData
import com.lorrdi.iqtest.data.models.Area
import com.lorrdi.iqtest.data.models.Employment
import com.lorrdi.iqtest.data.models.Experience
import com.lorrdi.iqtest.data.models.FiltersResponse
import com.lorrdi.iqtest.data.models.Region
import com.lorrdi.iqtest.data.models.Schedule
import com.lorrdi.iqtest.data.models.Vacancy
import kotlinx.coroutines.flow.Flow

interface VacancyRepository {
    fun getPagedVacancies(
        query: String? = null,
        experience: List<Experience>? = null,
        employment: List<Employment>? = null,
        schedule: List<Schedule>? = null,
        area: List<String>? = null,
        orderBy: String
    ): Flow<PagingData<Vacancy>>

    suspend fun getFilters(): FiltersResponse
    suspend fun getAreas(): List<Region>
}
