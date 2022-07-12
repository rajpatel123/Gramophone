package agstack.gramophone.utils

import android.content.SharedPreferences
import agstack.gramophone.utils.SharedPreferencesHelper
import android.content.Context
import kotlin.jvm.Synchronized

/**
 * Created by Abhisek on 04-03-2019.
 */

class SharedPreferencesHelper(context: Context) {
    private val editor: SharedPreferences.Editor
    private val sharedPreferences: SharedPreferences
    fun putString(key: String?, value: String?) {
        editor.putString(key, value)
        editor.apply()
    }

    fun putBoolean(key: String?, value: Boolean?) {
        editor.putBoolean(key, value!!)
        editor.apply()
    }

    fun putLong(key: String?, value: Long) {
        editor.putLong(key, value)
        editor.apply()
    }

    fun putFloat(key: String?, value: Float) {
        editor.putFloat(key, value)
        editor.apply()
    }

    fun removeId(id: String?) {
        editor.remove(id)
        editor.apply()
    }

    fun getString(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun getBoolean(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getLong(key: String?): Long {
        return sharedPreferences.getLong(key, 0)
    }

    fun getFloat(key: String?): Float {
        return sharedPreferences.getFloat(key, 0f)
    }

    fun getStringorDefault(key: String?, defaultVal: String?): String? {
        return sharedPreferences.getString(key, defaultVal)
    }

    companion object {
        private const val PREF_NAME = "sharedPreferences"
        private var sInstance: SharedPreferencesHelper? = null
        @JvmStatic
        @Synchronized
        fun initializeInstance(context: Context) {
            if (sInstance == null) {
                sInstance = SharedPreferencesHelper(context)
            }
        }

        @JvmStatic
        @get:Synchronized
        val instance: SharedPreferencesHelper?
            get() {
                checkNotNull(sInstance) {
                    SharedPreferencesHelper::class.java.simpleName +
                            " is not initialized, call initializeInstance(..) method first."
                }
                return sInstance
            }
    }

    init {
        editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}