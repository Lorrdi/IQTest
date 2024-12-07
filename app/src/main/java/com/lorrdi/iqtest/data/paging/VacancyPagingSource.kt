package com.lorrdi.iqtest.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lorrdi.iqtest.data.api.HhApiService
import com.lorrdi.iqtest.data.models.Employment
import com.lorrdi.iqtest.data.models.Experience
import com.lorrdi.iqtest.data.models.Schedule
import com.lorrdi.iqtest.data.models.Vacancy
import retrofit2.HttpException
import java.io.IOException

class VacancyPagingSource(
    private val hhApiService: HhApiService,
    private val query: String?,
    private val experience: List<Experience>? = null,
    private val employment: List<Employment>? = null,
    private val schedule: List<Schedule>? = null,
    private val area: List<String>? = null,
    private val orderBy: String = "relevance"
) : PagingSource<Int, Vacancy>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        val page = params.key ?: 1
        return try {
            val response = hhApiService.getVacancies(
                query = query,
                experience = experience?.firstOrNull()?.id,
                employment = employment?.firstOrNull()?.id,
                schedule = schedule?.firstOrNull()?.id,
                area = area?.joinToString(",") ?: "1",
                page = page,
                perPage = params.loadSize,
                orderBy = orderBy
            )
            LoadResult.Page(
                data = response.items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.items.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Vacancy>): Int? {
        return state.anchorPosition
    }
}

