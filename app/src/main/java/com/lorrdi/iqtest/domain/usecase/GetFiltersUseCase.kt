package com.lorrdi.iqtest.domain.usecase

import com.lorrdi.iqtest.data.api.HhApiService
import com.lorrdi.iqtest.data.models.FiltersResponse
import com.lorrdi.iqtest.domain.repositories.VacancyRepository
import javax.inject.Inject

class GetFiltersUseCase @Inject constructor(
    private val repository: VacancyRepository
) {
    suspend operator fun invoke(): FiltersResponse {
        return repository.getFilters()
    }
}
