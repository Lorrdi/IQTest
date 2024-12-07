package com.lorrdi.iqtest.domain.usecase

import com.lorrdi.iqtest.data.api.HhApiService
import com.lorrdi.iqtest.data.models.Region
import com.lorrdi.iqtest.domain.repositories.VacancyRepository
import javax.inject.Inject

class GetAreasUseCase @Inject constructor(
    private val repository: VacancyRepository
) {
    suspend operator fun invoke(): List<Region> {
        return repository.getAreas()
    }
}
