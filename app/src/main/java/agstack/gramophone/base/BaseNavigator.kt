package agstack.gramophone.base

import agstack.gramophone.ui.home.view.fragments.market.model.RelatedProductItem
import android.app.Activity
import android.location.Geocoder
import android.os.Bundle
import android.view.View

/*


BaseNavigator is the Thin Layer Between View and ViewModel

ViewModel ~~~~~~~View~~~~~~~Model


*/


interface BaseNavigator {

    fun <T> openActivity(cls: Class<T>, extras: Bundle? = null)

    fun <T> openAndFinishActivity(cls:Class<T>,extras: Bundle?=null)

    fun<T: Activity> openActivityWithBottomToTopAnimation(cls:Class<T>,extras: Bundle?=null)

    fun <T> openAndFinishActivityWithClearTopNewTaskClearTaskFlags(cls: Class<T>, extras: Bundle? = null)

    fun isNetworkAvailable(): Boolean

    fun requestPermission(permission: String): Boolean
   /* fun checkSelfPermissions( permission: String,function: (Boolean) -> Unit) : Boolean*/

    fun getLanguage(): String

    fun getMessage(stringResourceId: Int): String

    fun showToast(stringResourceId: Int)

    fun showToast(message: String?)

    fun onError(message: String?)

    fun onSuccess(message: String?)

    fun onLoading()

    fun hideProgressBar()

    fun proceedCall(helpLineNo: String, screenName: String)

    fun proceedOnLocationSetting()

    fun finishActivity()

    fun getGeoCoder(): Geocoder?

    fun checkPermission(permission: String): Boolean

    fun hideSoftKeyboard(view : View){}

    fun showSoftKeyboard(view : View){}
}