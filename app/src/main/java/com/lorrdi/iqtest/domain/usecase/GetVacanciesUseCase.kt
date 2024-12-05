package com.lorrdi.iqtest.domain.usecase

import com.lorrdi.iqtest.data.api.HhApiService
import com.lorrdi.iqtest.data.models.VacanciesResponse
import javax.inject.Inject

class GetVacanciesUseCase @Inject constructor(
    private val hhApiService: HhApiService
) {

    suspend operator fun invoke(
        query: String?,
        experience: String?,
        employment: String?,
        schedule: String?,
        area: String?,
        page: Int,
        perPage: Int
    ): VacanciesResponse {
        return hhApiService.getVacancies(
            query,
            experience,
            employment,
            schedule,
            area,
            page,
            perPage
        )
    }
}
