package agstack.gramophone.ui.checkout

import agstack.gramophone.base.BaseNavigator
import android.os.Bundle

interface CheckoutStatusNavigator : BaseNavigator {
    fun getBundle(): Bundle?

    fun openOrderListActivity()
}
