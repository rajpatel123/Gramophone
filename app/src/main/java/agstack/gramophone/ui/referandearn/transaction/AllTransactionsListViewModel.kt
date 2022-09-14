package agstack.gramophone.ui.referandearn.transaction

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.referandearn.transaction.model.GpApiResponseDataItem
import agstack.gramophone.utils.Constants
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AllTransactionsListViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : BaseViewModel<AllTransactionListNavigator>() {
    private var getGramCashTxnJob: Job? = null

    var progressLoader = ObservableField<Boolean>(false)
    var gramCashTxnResponseData = ObservableField<List<GpApiResponseDataItem?>?>()
    var selectedTab = ObservableField<Int>(0)
    var allListSize = ObservableField<Int>(0)
    var referralsListSize = ObservableField<Int>(0)
    var type: String = "all"
    fun getGramCashTransactionData() {
        getGramCashTxnJob.cancelIfActive()
        getGramCashTxnJob = checkNetworkThenRun {
            progressLoader.set(true)
            try {

                val allGramCashResponse = settingsRepository.getGramCashTxn(Constants.ALL_STRING)
                val referralGramCashResponse = settingsRepository.getGramCashTxn(Constants.REFERRAL)

                if (allGramCashResponse.body()?.gpApiStatus!!.equals(Constants.GP_API_STATUS)) {
                    progressLoader.set(false)
                    val gpApiResponseData = allGramCashResponse.body()?.gpApiResponseData
                    gramCashTxnResponseData.set(gpApiResponseData)
                    getNavigator()?.showToast(allGramCashResponse.body()?.gpApiMessage)
                } else {
                    progressLoader.set(false)
                    getNavigator()?.showToast(allGramCashResponse.body()?.gpApiMessage)
                }
            } catch (ex: Exception) {
                progressLoader.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
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