package com.lorrdi.iqtest.data.api

import com.lorrdi.iqtest.data.models.FiltersResponse
import com.lorrdi.iqtest.data.models.Region
import com.lorrdi.iqtest.data.models.VacanciesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HhApiService {
    @GET("vacancies")
    suspend fun getVacancies(
        @Query("text") query: String?,
        @Query("experience") experience: String?,
        @Query("employment") employment: String?,
        @Query("schedule") schedule: String?,
        @Query("area") area: String?,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): VacanciesResponse

    @GET("dictionaries")
    suspend fun getFilters(): FiltersResponse

    @GET("areas")
    suspend fun getAreas(): List<Region>

}