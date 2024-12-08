package com.lorrdi.iqtest.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lorrdi.iqtest.data.dto.ErrorState
import com.lorrdi.iqtest.data.dto.Vacancy
import com.lorrdi.iqtest.data.dto.VacancyRequestParams
import com.lorrdi.iqtest.data.remote.HhApiService
import retrofit2.HttpException
import java.io.IOException

class VacancyPagingSource(
    private val hhApiService: HhApiService,
    private val requestParams: VacancyRequestParams
) : PagingSource<Int, Vacancy>() {

    companion object {
        private const val HTTP_UNAUTHORIZED = 401
        private const val HTTP_FORBIDDEN = 403
        private const val HTTP_NOT_FOUND = 404
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        val page = params.key ?: requestParams.page

        return try {
            val response = hhApiService.getVacancies(
                query = requestParams.query,
                experience = requestParams.experience,
                employment = requestParams.employment,
                schedule = requestParams.schedule,
                area = requestParams.area,
                page = page,
                perPage = requestParams.perPage,
                orderBy = requestParams.orderBy
            )
            LoadResult.Page(
                data = response.items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.items.isEmpty()) null else page + 1
            )
        } catch (_: IOException) {
            LoadResult.Error(PagingException(ErrorState.NetworkError))
        } catch (exception: HttpException) {
            val errorState = when (exception.code()) {
                HTTP_UNAUTHORIZED -> ErrorState.ServerError(code = HTTP_UNAUTHORIZED)
                HTTP_FORBIDDEN -> ErrorState.ServerError(code = HTTP_FORBIDDEN)
                HTTP_NOT_FOUND -> ErrorState.ServerError(code = HTTP_NOT_FOUND)
                else -> ErrorState.UnknownError(message = exception.message())
            }
            LoadResult.Error(PagingException(errorState))
        } catch (exception: Exception) {
            LoadResult.Error(
                PagingException(
                    ErrorState.UnknownError(
                        message = exception.localizedMessage ?: "Unknown error"
                    )
                )
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Vacancy>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
