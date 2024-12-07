package com.lorrdi.iqtest.domain.usecase

import com.lorrdi.iqtest.data.dto.Filters
import com.lorrdi.iqtest.domain.repositories.VacancyRepository
import javax.inject.Inject

class GetFiltersUseCase @Inject constructor(
    private val repository: VacancyRepository
) {
    suspend operator fun invoke(): Filters {
        return repository.getFilters()
    }
}
