package com.turkcell.lyraapp.di

import com.turkcell.lyraapp.data.home.FakeHomeRepository
import com.turkcell.lyraapp.data.home.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * [HomeRepository] arayüzünü [FakeHomeRepository] implementasyonuna bağlar.
 *
 * Gerçek API geldiğinde yalnızca @Binds hedefi değiştirilir.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: FakeHomeRepository): HomeRepository
}
