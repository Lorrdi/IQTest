package com.lorrdi.iqtest.domain.usecase

import com.lorrdi.iqtest.data.dto.Region
import com.lorrdi.iqtest.domain.repositories.VacancyRepository
import javax.inject.Inject

class GetAreasUseCase @Inject constructor(
    private val repository: VacancyRepository
) {
    suspend operator fun invoke(): List<Region> {
        return repository.getAreas()
    }
}
