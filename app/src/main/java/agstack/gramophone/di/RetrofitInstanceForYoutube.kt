package agstack.gramophone.di

import agstack.gramophone.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstanceForYoutube {
    companion object {
        private var retrofit: Retrofit? = null

        @Synchronized
        fun getInstance(): Retrofit? {
            if (retrofit == null) {
                val clientBuilder2 = OkHttpClient.Builder()
                clientBuilder2.connectTimeout(60, TimeUnit.SECONDS)
                clientBuilder2.readTimeout(60, TimeUnit.SECONDS)
                clientBuilder2.writeTimeout(60, TimeUnit.SECONDS)
                clientBuilder2.addInterceptor(YoutubeTokenInterceptor())

                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                clientBuilder2.addInterceptor(httpLoggingInterceptor)

                retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL_YOUTUBE)
                    .client(clientBuilder2.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }
}