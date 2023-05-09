package agstack.gramophone.utils

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkInterceptor(context: Context) : Interceptor {
    private val mApplicationContext: Context = context.applicationContext

    private val isConnected: Boolean
        get() {
            val cm = mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (!isConnected) {
            throw NoNetworkException()
        }
        return chain.proceed(originalRequest)
    }

    class NoNetworkException internal constructor() : RuntimeException("Please check Network Connection")
}