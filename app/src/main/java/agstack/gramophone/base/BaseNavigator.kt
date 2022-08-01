package agstack.gramophone.base

import android.app.Activity
import android.os.Bundle

/*


BaseNavigator is the Thin Layer Between View and ViewModel

ViewModel ~~~~~~~View~~~~~~~Model


*/


interface BaseNavigator {

    fun <T> openActivity(cls: Class<T>, extras: Bundle? = null)

    fun <T> openAndFinishActivity(cls:Class<T>,extras: Bundle?=null)
    fun<T: Activity> openActivityWithBottomToTopAnimation(cls:Class<T>,extras: Bundle?=null)
    fun <T> openAndFinishActivity(cls: Class<T>, extras: Bundle? = null)

    fun <T> openAndFinishActivityWithFlags(cls: Class<T>, extras: Bundle? = null)

    fun isNetworkAvailable(): Boolean

    fun requestPermission(permission: String): Boolean

    fun getLanguage(): String

    fun getMessage(stringResourceId: Int): String

    fun showToast(stringResourceId: Int)

    fun showToast(message: String?)

    fun onError(message: String?)

    fun onSuccess(message: String?)

    fun onLoading()

    fun hideProgressBar()


}