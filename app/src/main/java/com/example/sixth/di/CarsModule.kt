package com.example.sixth.di

import com.example.sixth.config.SixtRequestExecutor
import com.example.sixth.modules.cars.CarsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CarsModule {

    @Singleton
    @Provides
    fun provideCarRepository(
        sixtRequestExecutor: SixtRequestExecutor
    ) = CarsRepository(
        sixtRequestExecutor
    )
}
