package agstack.gramophone.di


import agstack.gramophone.data.repository.articles.ArticlesRepository
import agstack.gramophone.data.repository.articles.ArticlesRepositoryImpl
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.data.repository.community.CommunityRepositoryImpl
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.data.repository.onboarding.OnBoardingRepositoryImpl
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.data.repository.product.ProductRepositoryImpl
import agstack.gramophone.data.repository.promotions.PromotionsRepository
import agstack.gramophone.data.repository.promotions.PromotionsRepositoryImpl
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.data.repository.settings.SettingsRepositoryImpl
import agstack.gramophone.data.repository.tv.GramophoneTVRepository
import agstack.gramophone.data.repository.tv.GramophoneTVRepositoryImpl
import agstack.gramophone.data.repository.weather.WeatherRepository
import agstack.gramophone.data.repository.weather.WeatherRepositoryImpl
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

    @Binds
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository


    @Binds
    fun bindCommunityRepository(impl: CommunityRepositoryImpl): CommunityRepository

    @Binds
    fun bindPromotionsRepository(impl: PromotionsRepositoryImpl): PromotionsRepository

    @Binds
    fun bindArticlesRepository(impl: ArticlesRepositoryImpl): ArticlesRepository

    @Binds
    fun bindGramophoneTVRepository(impl: GramophoneTVRepositoryImpl): GramophoneTVRepository
}