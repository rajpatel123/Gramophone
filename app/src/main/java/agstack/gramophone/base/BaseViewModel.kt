package agstack.gramophone.base

import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.Manifest
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference



open class BaseViewModel< N:BaseNavigator>():ViewModel() {

    private var mNavigator: WeakReference<N?> = WeakReference(null)


    internal fun setNavigator(navigator: N?){
        mNavigator = WeakReference(navigator)
    }


    fun getNavigator() = mNavigator.get()

    internal fun Job?.cancelIfActive() = this?.takeIf { it.isActive }?.cancel()

    fun onHelpClick() {
        if (getNavigator()?.requestPermission(Manifest.permission.CALL_PHONE) == true) {
            val supportNo: String? =
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CustomerSupportNo)
            if (supportNo?.isNotEmpty() == true) {
                getNavigator()?.proceedCall(supportNo)
            }
        }
    }

}