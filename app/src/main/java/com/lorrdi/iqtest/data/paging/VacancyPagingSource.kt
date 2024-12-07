package com.lorrdi.iqtest.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lorrdi.iqtest.data.remote.HhApiService
import com.lorrdi.iqtest.data.dto.Employment
import com.lorrdi.iqtest.data.dto.Experience
import com.lorrdi.iqtest.data.dto.Schedule
import com.lorrdi.iqtest.data.dto.Vacancy
import retrofit2.HttpException
import java.io.IOException

class VacancyPagingSource(
    private val hhApiService: HhApiService,
    private val query: String?,
    private val experience: List<Experience>? = null,
    private val employment: List<Employment>? = null,
    private val schedule: List<Schedule>? = null,
    private val area: List<String>? = null,
    private val orderBy: String = DEFAULT_ORDER_BY
) : PagingSource<Int, Vacancy>() {

    companion object {
        const val DEFAULT_AREA = "1"
        const val DEFAULT_ORDER_BY = "relevance"
        const val FIRST_PAGE = 1
        private const val HTTP_UNAUTHORIZED = 401
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        val page = params.key ?: FIRST_PAGE
        return try {
            val response = hhApiService.getVacancies(
                query = query,
                experience = experience?.firstOrNull()?.id,
                employment = employment?.firstOrNull()?.id,
                schedule = schedule?.firstOrNull()?.id,
                area = area?.joinToString(","),
                page = page,
                perPage = params.loadSize,
                orderBy = orderBy
            )
            LoadResult.Page(
                data = response.items,
                prevKey = if (page == FIRST_PAGE) null else page - 1,
                nextKey = if (response.items.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            when (exception.code()) {
                HTTP_UNAUTHORIZED -> LoadResult.Error(Exception("Unauthorized access"))
                else -> LoadResult.Error(exception)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Vacancy>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}

