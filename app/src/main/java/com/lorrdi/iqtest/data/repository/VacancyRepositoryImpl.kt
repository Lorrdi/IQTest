package com.lorrdi.iqtest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lorrdi.iqtest.data.api.HhApiService
import com.lorrdi.iqtest.data.models.Employment
import com.lorrdi.iqtest.data.models.Experience
import com.lorrdi.iqtest.data.models.FiltersResponse
import com.lorrdi.iqtest.data.models.Region
import com.lorrdi.iqtest.data.models.Schedule
import com.lorrdi.iqtest.data.models.Vacancy
import com.lorrdi.iqtest.data.paging.VacancyPagingSource
import com.lorrdi.iqtest.domain.repositories.VacancyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VacancyRepositoryImpl @Inject constructor(
    private val hhApiService: HhApiService
) : VacancyRepository {

    override fun getPagedVacancies(
        query: String?,
        experience: List<Experience>?,
        employment: List<Employment>?,
        schedule: List<Schedule>?,
        area: List<String>?,
        orderBy: String
    ): Flow<PagingData<Vacancy>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                VacancyPagingSource(
                    hhApiService = hhApiService,
                    query = query,
                    experience = experience,
                    employment = employment,
                    schedule = schedule,
                    area = area,
                    orderBy = orderBy
                )
            }
        ).flow
    }

    override suspend fun getFilters(): FiltersResponse {
        return hhApiService.getFilters()
    }

    override suspend fun getAreas(): List<Region> {
        return hhApiService.getAreas()
    }
}
