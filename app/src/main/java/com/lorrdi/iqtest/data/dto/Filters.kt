package com.lorrdi.iqtest.data.dto

data class Filters(
    val experience: List<Experience>? = null,
    val employment: List<Employment>? = null,
    val schedule: List<Schedule>? = null,
    val area: List<String>? = null
)