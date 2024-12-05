package com.lorrdi.iqtest.data.models

data class Salary(
    val from: Long,
    val to: Long?,
    val currency: String,
    val gross: Boolean,
)