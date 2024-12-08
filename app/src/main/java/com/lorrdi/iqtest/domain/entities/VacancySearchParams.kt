package com.lorrdi.iqtest.domain.entities

import com.lorrdi.iqtest.data.dto.Filters
import com.lorrdi.iqtest.data.dto.VacancyRequestParams
import com.lorrdi.iqtest.domain.enums.SortingOption

data class VacancySearchParams(
    val query: String?,
    val filters: Filters?,
    val sorting: SortingOption
) {
    companion object {
        const val DEFAULT_AREA = "1"
    }
}
fun VacancySearchParams.toRequestParams(): VacancyRequestParams {
    return VacancyRequestParams(
        query = this.query,
        experience = this.filters?.experience?.firstOrNull()?.id,
        employment = this.filters?.employment?.firstOrNull()?.id,
        schedule = this.filters?.schedule?.firstOrNull()?.id,
        area = this.filters?.area?.joinToString(","),
        orderBy = this.sorting.apiValue
    )
}