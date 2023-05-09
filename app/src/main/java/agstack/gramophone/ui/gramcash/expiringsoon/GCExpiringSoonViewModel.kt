package agstack.gramophone.ui.gramcash.expiringsoon

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.home.view.LostConnectionActivity
import agstack.gramophone.ui.referandearn.transaction.GramCashTransactionListAdapter
import agstack.gramophone.ui.referandearn.transaction.TransactionRequestModel
import agstack.gramophone.ui.referandearn.transaction.model.GramcashTxnItem
import agstack.gramophone.utils.Constants
import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class GCExpiringSoonViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<GCExpiringSoonNavigator>() {

    var progressLoader = ObservableField<Boolean>(false)
    private var getGramCashTxnJob: Job? = null
    var mAllTxnList = ArrayList<GramcashTxnItem?>()
    var itemCount = MutableLiveData<Int>()
    var isLastPage = false
    var Gramcashexpiring_soon_amount = ObservableField<String>()

    init {
        itemCount.value = 1
    }

    fun getGramCashTxnExpiringSoon(GC_Expiring_soon_amount :String?) {
        Gramcashexpiring_soon_amount.set(GC_Expiring_soon_amount)

        if (getGramCashTxnJob!=null){
            getGramCashTxnJob.cancelIfActive()
        }
        getGramCashTxnJob = checkNetworkThenRun {
            progressLoader.set(true)
            try {

                var txnRequestBody = TransactionRequestModel()
                txnRequestBody.page = "1"
                val allGramCashTxnResponse =
                    settingsRepository.getGramCashTxn(Constants.EXPIRE, txnRequestBody)


                if (allGramCashTxnResponse.body()?.gpApiStatus!!.equals(Constants.GP_API_STATUS)) {
                    progressLoader.set(false)
                    val gpApiResponseDataList: List<GramcashTxnItem?>? =
                        allGramCashTxnResponse.body()?.gpApiResponseData?.gramcashTxn
                    mAllTxnList =
                        allGramCashTxnResponse.body()?.gpApiResponseData?.gramcashTxn as ArrayList<GramcashTxnItem?>
                    itemCount.value = mAllTxnList.size
                    getNavigator()?.setExpireTxnListAdapter(GramCashTransactionListAdapter(mAllTxnList))
                   // getNavigator()?.showToast(allGramCashTxnResponse.body()?.gpApiMessage)
                } else {
                    progressLoader.set(false)
                    getNavigator()?.showToast(allGramCashTxnResponse.body()?.gpApiMessage)
                }




            } catch (ex: Exception) {
                progressLoader.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
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

    @SuppressLint("SuspiciousIndentation")
    fun loadMore(currentPage: Int) {
        var txnRequestBody = TransactionRequestModel()
        txnRequestBody.page = currentPage.toString()

            if (getGramCashTxnJob!=null){
                getGramCashTxnJob.cancelIfActive()
            }
            getNavigator()?.showLoaderFooter()
            getGramCashTxnJob = viewModelScope.launch {


                val allGramCashTxnResponse =
                    settingsRepository.getGramCashTxn(Constants.EXPIRE, txnRequestBody)

                if (allGramCashTxnResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                    mAllTxnList.addAll(allGramCashTxnResponse.body()?.gpApiResponseData?.gramcashTxn as ArrayList<GramcashTxnItem?>)
                    val lastpage = allGramCashTxnResponse.body()?.gpApiResponseData?.lastPage
                    if (currentPage == lastpage) {
                        isLastPage = true

                    }
                    getNavigator()?.onListUpdated()
                } else {
                    getNavigator()?.onListUpdated()
                }


        }
    }


    fun onRedeemNowClicked(){
        getNavigator()?.goBack()
    }
}