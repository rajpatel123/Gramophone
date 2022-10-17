package agstack.gramophone.di

import agstack.gramophone.BuildConfig
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(getHeaderInterceptor())
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS) // connect timeout
            .readTimeout(70, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("mobility")
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("community")
    fun provideRetrofitCommunity(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_SOCIAL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    @Provides
    @Singleton
    @Named("promotions")
    fun provideRetrofitPromotions(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_PROMOTION)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("articles")
    fun provideRetrofitArticles(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_ARTICLES_BLOG)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideGramAppService(@Named("mobility") retrofit: Retrofit): GramAppService {
        return retrofit.create(GramAppService::class.java)
    }

    @Provides
    @Singleton
    fun provideGramAppSocialService(@Named("community") retrofit: Retrofit): CommunityApiService {
        return retrofit.create(CommunityApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideGramAppPromotionsService(@Named("promotions") retrofit: Retrofit): PromotionsApiService {
        return retrofit.create(PromotionsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGramAppArticlesService(@Named("articles") retrofit: Retrofit): ArticlesApiService {
        return retrofit.create(ArticlesApiService::class.java)
    }

    private fun getHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .header(
                        Constants.AUTHORIZATION,
                        "Bearer " + SharedPreferencesHelper.instance?.getString(
                            SharedPreferencesKeys.session_token
                        )!!
                    )
                    .build()
            chain.proceed(request)
        }
    }

    private fun getCommunityHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .header(
                        Constants.AUTHORIZATION,
                        "Bearer " + SharedPreferencesHelper.instance?.getString(
                            SharedPreferencesKeys.session_token
                        )!!
                    )
                    .build()
            chain.proceed(request)
        }
    }

    /*   @Provides
       @Singleton
       @Named("community")
       fun provideCommunityOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
           return OkHttpClient.Builder()
               .addInterceptor(getCommunityHeaderInterceptor())
               .addInterceptor(logging)
               .connectTimeout(15, TimeUnit.SECONDS) // connect timeout
               .readTimeout(15, TimeUnit.SECONDS)
               .build()
       }

       @Provides
       @Singleton
       @Named("community")
       fun provideCommunityRetrofit(@Named("community") client: OkHttpClient): Retrofit {
           return Retrofit.Builder()
               .baseUrl(BuildConfig.BASE_URL_SOCIAL)
               .addConverterFactory(GsonConverterFactory.create())
               .client(client)
               .build()
       }

       @Provides
       @Singleton
       fun provideCommunityService(@Named("community") retrofit: Retrofit): CommunityAppService {
           return retrofit.create(CommunityAppService::class.java)
       }*/


}