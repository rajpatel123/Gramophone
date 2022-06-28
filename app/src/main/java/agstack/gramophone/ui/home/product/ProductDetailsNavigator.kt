package agstack.gramophone.ui.home.product

import agstack.gramophone.base.BaseNavigator
import android.os.Bundle

interface ProductDetailsNavigator: BaseNavigator {
    fun getBundle(): Bundle?
    fun setToolbarTitle(title:String?)
}