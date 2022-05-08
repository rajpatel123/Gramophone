package agstack.gramophone.utils

import agstack.gramophone.BuildConfig
import android.content.SharedPreferences
import agstack.gramophone.utils.SharedHelper
import android.content.Context

object SharedHelper {
   private lateinit var sharedPreferences: SharedPreferences
   private lateinit var editor: SharedPreferences.Editor
    fun putKey(context: Context, Key: String?, Value: String?) {
        sharedPreferences =
            context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString(Key, Value)
        editor.apply()
    }

    fun getKey(context: Context, Key: String?): String? {
        sharedPreferences =
            context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        return sharedPreferences.getString(Key, "")
    }

    fun putKey(context: Context, Key: String?, Value: Boolean) {
        sharedPreferences =
            context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putBoolean(Key, Value)
        editor.apply()
    }

    fun putKey(context: Context, Key: String?, value: Int?) {
        sharedPreferences =
            context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putInt(Key, value!!)
        editor.apply()
    }

    fun getIntKey(context: Context, Key: String?): Int {
        sharedPreferences =
            context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(Key, -1)
    }

    fun getBoolKey(context: Context, Key: String?, defalultValue: Boolean): Boolean {
        sharedPreferences =
            context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Key, defalultValue)
    }

    fun getKey(context: Context, Key: String?, defaultValue: String?): String? {
        sharedPreferences =
            context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        return sharedPreferences.getString(Key, defaultValue)
    }

    fun clearSharedPreferences(context: Context) {
        val device_token = getKey(context, "device_token").toString()
        val device_id = getKey(context, "device_id").toString()
        sharedPreferences =
            context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        putKey(context, "device_token", device_token)
        putKey(context, "device_id", device_id)
    }
}