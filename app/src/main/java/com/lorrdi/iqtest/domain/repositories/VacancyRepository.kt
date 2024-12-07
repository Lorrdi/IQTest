package com.lorrdi.iqtest.domain.repositories

import androidx.paging.PagingData
import com.lorrdi.iqtest.data.dto.Employment
import com.lorrdi.iqtest.data.dto.Experience
import com.lorrdi.iqtest.data.dto.Filters
import com.lorrdi.iqtest.data.dto.Region
import com.lorrdi.iqtest.data.dto.Schedule
import com.lorrdi.iqtest.data.dto.Vacancy
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

    suspend fun getFilters(): Filters
    suspend fun getRegions(): List<Region>
}
