package agstack.gramophone.di

import agstack.gramophone.data.repository.onboarding.OnboardingRepository
import agstack.gramophone.data.repository.onboarding.OnboardingRepositoryImpl
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.data.repository.product.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository

    @Binds
    fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

}