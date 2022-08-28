package agstack.gramophone.ui.feedback

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.utils.Constants
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val repository: ProductRepository
) : BaseViewModel<FeedbackNavigator>() {

    private var feedbackJob: Job? = null
    var progressLoader = ObservableField<Boolean>(false)

    fun onSendClick() {
        if (getNavigator()?.getFeedbackText() != null) {

            Log.d("Click", "feebacksendClicked")
            feedbackJob.cancelIfActive()
            feedbackJob = checkNetworkThenRun {
                progressLoader.set(true)
                var producttoBeAdded = ProductData()
                /* producttoBeAdded.product_id = productId
                  val expertAdviceResponse =
                      repository.getExpertAdvice(producttoBeAdded)
                  progressLoader.set(false)

                  if (expertAdviceResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {

                      getNavigator()?.showToast(expertAdviceResponse.body()?.gp_api_message)
                  } else {
                      getNavigator()?.showToast(expertAdviceResponse.body()?.gp_api_message)
                  }*/


                getNavigator()?.finishActivity()
            }
        } else {
            getNavigator()?.showToast(R.string.please_enter_feedback)
        }

    }

    private fun checkNetworkThenRun(runCode: (suspend () -> Unit)): Job {
        return viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    runCode.invoke()
                } else {
                    getNavigator()?.showToast(R.string.nointernet)
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

}