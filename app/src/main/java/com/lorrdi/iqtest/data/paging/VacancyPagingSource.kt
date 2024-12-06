package com.lorrdi.iqtest.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lorrdi.iqtest.data.api.HhApiService
import com.lorrdi.iqtest.data.models.Area
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
) : PagingSource<Int, Vacancy>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        val page = params.key ?: 1
        return try {
            area?.joinToString(",")?.let { Log.d("DEBUG_A", it) }
            val response = hhApiService.getVacancies(
                query = query,
                experience = experience?.firstOrNull()?.id, // Safely get the first item, or null if the list is empty
                employment = employment?.firstOrNull()?.id, // Same for employment
                schedule = schedule?.firstOrNull()?.id, // Same for schedule
                area = area?.joinToString(","),
                page = page,
                perPage = params.loadSize
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

