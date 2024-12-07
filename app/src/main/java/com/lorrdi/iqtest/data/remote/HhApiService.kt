package com.lorrdi.iqtest.data.remote

import com.lorrdi.iqtest.data.dto.Filters
import com.lorrdi.iqtest.data.dto.Region
import com.lorrdi.iqtest.data.dto.VacanciesResponse
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
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy:String
    ): VacanciesResponse

    @GET("dictionaries")
    suspend fun getFilters(): Filters

    @GET("areas")
    suspend fun getAreas(): List<Region>

}