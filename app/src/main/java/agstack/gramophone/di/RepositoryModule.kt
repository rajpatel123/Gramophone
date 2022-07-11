package agstack.gramophone.di

import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.data.repository.onboarding.OnBoardingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindOnBoardingRepository(impl: OnBoardingRepositoryImpl):OnBoardingRepository

}