package com.lorrdi.iqtest.data.models

data class FiltersResponse(
    val experience: List<Experience>? = null,
    val employment: List<Employment>? = null,
    val schedule: List<Schedule>? = null,
    val area: List<String>? = null
)