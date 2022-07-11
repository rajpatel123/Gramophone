package agstack.gramophone.di

import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindOnboardingRepository(impl: OnBoardingRepositoryImpl ):OnBoardingRepository

}