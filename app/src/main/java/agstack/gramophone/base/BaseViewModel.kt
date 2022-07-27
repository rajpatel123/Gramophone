package agstack.gramophone.base

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

}