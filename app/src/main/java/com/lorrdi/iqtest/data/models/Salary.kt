package com.lorrdi.iqtest.data.models

data class Salary(
    val from: Long?,
    val to: Long?,
    val currency: String,
    val gross: Boolean
) {
    fun isWithinRange(range: ClosedFloatingPointRange<Float>): Boolean {
        val minSalary = from ?: 0L
        val maxSalary = to ?: Long.MAX_VALUE
        return range.start <= minSalary && range.endInclusive >= maxSalary
    }
}