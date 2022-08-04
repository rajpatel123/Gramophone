package agstack.gramophone.ui.checkout

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.utils.Constants
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutStatusViewModel @Inject constructor(
) : BaseViewModel<CheckoutStatusNavigator>() {

    var orderId = MutableLiveData<String>()

    init {
        orderId.value = "N.A"
    }

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.containsKey(Constants.ORDER_ID)!! && bundle.getString(Constants.ORDER_ID) != null) {
            orderId.value = bundle.get(Constants.ORDER_ID) as String
        }
    }

    fun onTrackOrderClick() {
        getNavigator()?.openOrderListActivity()
    }

}
