package com.lorrdi.iqtest.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lorrdi.iqtest.data.api.HhApiService
import com.lorrdi.iqtest.data.models.Vacancy

class VacancyPagingSource(
    private val hhApiService: HhApiService,
    private val query: String?,
    private val experience: String?,
    private val employment: String?,
    private val schedule: String?,
    private val area: String?
) : PagingSource<Int, Vacancy>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        return try {
            val page = params.key ?: 1
            val perPage = params.loadSize

            val response = hhApiService.getVacancies(
                query = query,
                experience = experience,
                employment = employment,
                schedule = schedule,
                area = area,
                page = page,
                perPage = perPage
            )

            LoadResult.Page(
                data = response.items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Vacancy>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

