package com.lorrdi.iqtest.domain.repositories

import androidx.paging.PagingData
import com.lorrdi.iqtest.data.dto.Filters
import com.lorrdi.iqtest.data.dto.Region
import com.lorrdi.iqtest.data.dto.Vacancy
import com.lorrdi.iqtest.domain.entities.VacancySearchParams
import kotlinx.coroutines.flow.Flow

interface VacancyRepository {
    fun getPagedVacancies(params: VacancySearchParams): Flow<PagingData<Vacancy>>
    suspend fun getFilters(): Filters
    suspend fun getRegions(): List<Region>
}