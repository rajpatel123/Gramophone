package agstack.gramophone.di

import agstack.gramophone.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class YoutubeTokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        return if (BuildConfig.DEBUG) {
            val request = original.newBuilder()
                .header("X-Android-Package", "agstack.gramophone.debug")
                .header("X-Android-Cert", " 3378a4ccfe01cbb2bf08fae75dd45fb2efd5b532")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        } else {
            val request = original.newBuilder()
                .header("X-Android-Package", "agstack.gramophone")
                .header("X-Android-Cert", " 7b9c23941cfb08a6070de20614d4d7fab7f0f3b8")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }

    }
}