package com.lorrdi.iqtest.data.dto

data class VacancyRequestParams(
    val query: String?,
    val experience: String?,
    val employment: String?,
    val schedule: String?,
    val area: String?,
    val orderBy: String,
    val page: Int = 1,
    val perPage: Int = 20
)