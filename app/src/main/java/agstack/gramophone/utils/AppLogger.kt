package agstack.gramophone.utils

import android.util.Log
import androidx.viewbinding.BuildConfig

/**
 * Created by farhan on 7/03/2022.
 */
object AppLogger {
    /**
     * @param tag     give tag to filter a specific log
     * @param message result output in log
     */
    fun v(tag: String?, message: String?) {
        if (BuildConfig.DEBUG) Log.v(tag, message!!)
    }

    /**
     * @param tag     give tag to filter a specific log
     * @param message result output in log
     */
    fun d(tag: String?, message: String?, cause: Throwable?) {
        if (BuildConfig.DEBUG) Log.d(tag, message, cause)
    }

    /**
     * @param tag     give tag to filter a specific log
     * @param message result output in log
     */
    fun d(tag: String?, message: String?) {
        if (BuildConfig.DEBUG) Log.d(tag, message!!)
    }

    /**
     * @param tag     give tag to filter a specific log
     * @param message result output in log
     */
    fun i(tag: String?, message: String?) {
        if (BuildConfig.DEBUG) Log.i(tag, message!!)
    }

    /**
     * @param tag     give tag to filter a specific log
     * @param message result output in log
     */
    fun w(tag: String?, message: String?) {
        if (BuildConfig.DEBUG) Log.w(tag, message!!)
    }

    /**
     * @param tag     give tag to filter a specific log
     * @param message result output in log
     */
    fun e(tag: String?, message: String?) {
        if (BuildConfig.DEBUG && message != null) Log.e(tag, message)
    }
}