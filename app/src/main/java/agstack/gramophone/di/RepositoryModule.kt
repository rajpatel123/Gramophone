package agstack.gramophone.di


import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.data.repository.onboarding.OnBoardingRepositoryImpl
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.data.repository.product.ProductRepositoryImpl
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.data.repository.settings.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindOnBoardingRepository(impl: OnBoardingRepositoryImpl): OnBoardingRepository

    @Binds
    fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

}