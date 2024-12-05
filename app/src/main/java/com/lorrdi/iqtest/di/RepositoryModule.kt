package com.lorrdi.iqtest.di

import com.lorrdi.iqtest.data.repository.VacancyRepositoryImpl
import com.lorrdi.iqtest.domain.repositories.VacancyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVacancyRepository(
        vacancyRepositoryImpl: VacancyRepositoryImpl
    ): VacancyRepository
}
