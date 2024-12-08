package com.lorrdi.iqtest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lorrdi.iqtest.data.dto.Filters
import com.lorrdi.iqtest.data.dto.Region
import com.lorrdi.iqtest.data.dto.Vacancy
import com.lorrdi.iqtest.data.paging.VacancyPagingSource
import com.lorrdi.iqtest.data.remote.HhApiService
import com.lorrdi.iqtest.domain.entities.VacancySearchParams
import com.lorrdi.iqtest.domain.entities.toRequestParams
import com.lorrdi.iqtest.domain.repositories.VacancyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VacancyRepositoryImpl @Inject constructor(
    private val hhApiService: HhApiService
) : VacancyRepository {

    override fun getPagedVacancies(params: VacancySearchParams): Flow<PagingData<Vacancy>> {
        val requestParams = params.toRequestParams()
        return Pager(
            config = PagingConfig(
                pageSize = requestParams.perPage,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                VacancyPagingSource(
                    hhApiService = hhApiService,
                    requestParams = requestParams
                )
            }
        ).flow
    }

    override suspend fun getFilters(): Filters {
        return hhApiService.getFilters()
    }

    override suspend fun getRegions(): List<Region> {
        return hhApiService.getRegions()
    }
}

