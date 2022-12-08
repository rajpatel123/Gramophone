package agstack.gramophone.ui.notification.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.notification.NotificationNavigator
import agstack.gramophone.ui.notification.NotificationsAdapter
import agstack.gramophone.ui.notification.model.NotificationRequestModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Utility
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
):BaseViewModel<NotificationNavigator>(){
    val limit =10
    val page = 1
    val  progress = ObservableField<Boolean>(false)
    fun getNotification() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.set(true)
                    var producttoBeAdded = ProductData()
                    producttoBeAdded.product_id = null
                    producttoBeAdded.comments = ""

                    val notificationResponse = onBoardingRepository.getNotification(NotificationRequestModel(limit,page))
                    progress.set(false)


                    if (notificationResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                        getNavigator()?.updateNotificationList(NotificationsAdapter(
                            notificationResponse.body()!!.gp_api_response_data.data)){

                        }
                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(notificationResponse.errorBody()))
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }
}