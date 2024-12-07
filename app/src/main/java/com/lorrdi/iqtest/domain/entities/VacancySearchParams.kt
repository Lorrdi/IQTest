package com.lorrdi.iqtest.domain.entities

import com.lorrdi.iqtest.data.dto.Filters
import com.lorrdi.iqtest.domain.enums.SortingOption

data class VacancySearchParams(
    val query: String?,
    val filters: Filters?,
    val sorting: SortingOption
)
