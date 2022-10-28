package agstack.gramophone.ui.referandearn.transaction

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.referandearn.transaction.model.GramcashTxnItem
import agstack.gramophone.utils.Constants
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
class AllTransactionsListViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : BaseViewModel<AllTransactionListNavigator>() {
    private var getGramCashTxnJob: Job? = null

    var progressLoader = ObservableField<Boolean>(false)

    //Check if needed
    var gramCashAllTxnResponseData = ObservableField<List<GramcashTxnItem?>?>()
    var selectedTab = ObservableField<Int>(0)
    var allListSize = ObservableField<Int>(0)


    //Check if needed
    var gramCashReferralTxnResponseData = ObservableField<List<GramcashTxnItem?>?>()
    var referralsListSize = ObservableField<Int>(0)
    var emptyText = ObservableField<String>(getNavigator()?.getMessage(R.string.no_transactions))
    var type: String = "all"
    var isLastPage = false
    var isLastPageReferral = false
    var mAllTxnList = ArrayList<GramcashTxnItem?>()
    var mReferralTxnList = ArrayList<GramcashTxnItem?>()
    fun getGramCashTransactionData() {
        getGramCashTxnJob.cancelIfActive()
        getGramCashTxnJob = checkNetworkThenRun {
            progressLoader.set(true)
            try {

                var txnRequestBody = TransactionRequestModel()
                txnRequestBody.page = "1"
                val allGramCashTxnResponse =
                    settingsRepository.getGramCashTxn(Constants.ALL_STRING, txnRequestBody)
                val referralGramCashResponse =
                    settingsRepository.getGramCashTxn(Constants.REFERRAL, txnRequestBody)

                if (allGramCashTxnResponse.body()?.gpApiStatus!!.equals(Constants.GP_API_STATUS)) {
                    progressLoader.set(false)
                    val gpApiResponseDataList: List<GramcashTxnItem?>? =
                        allGramCashTxnResponse.body()?.gpApiResponseData?.gramcashTxn
                    gramCashAllTxnResponseData.set(gpApiResponseDataList)
                    allListSize.set(gpApiResponseDataList?.size)
                    mAllTxnList =
                        allGramCashTxnResponse.body()?.gpApiResponseData?.gramcashTxn as ArrayList<GramcashTxnItem?>
                    getNavigator()?.setAllTxnListAdapter(GramCashTransactionListAdapter(mAllTxnList))
                   // getNavigator()?.showToast(allGramCashTxnResponse.body()?.gpApiMessage)
                } else {
                    progressLoader.set(false)
                    getNavigator()?.showToast(allGramCashTxnResponse.body()?.gpApiMessage)
                }



                if (referralGramCashResponse.body()?.gpApiStatus!!.equals(Constants.GP_API_STATUS)) {
                    progressLoader.set(false)
                    gramCashReferralTxnResponseData.set(referralGramCashResponse.body()?.gpApiResponseData?.gramcashTxn)
                    mReferralTxnList =
                        referralGramCashResponse.body()?.gpApiResponseData?.gramcashTxn as ArrayList<GramcashTxnItem?>
                    referralsListSize.set(referralGramCashResponse.body()?.gpApiResponseData?.gramcashTxn?.size)
                    getNavigator()?.setReferralTxnListAdapter(
                        GramCashTransactionListAdapter(
                            mReferralTxnList
                        )
                    )
                    //getNavigator()?.showToast(referralGramCashResponse.body()?.gpApiMessage)

                } else {
                    progressLoader.set(false)
                    getNavigator()?.showToast(referralGramCashResponse.body()?.gpApiMessage)
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

    fun loadMore(currentPage: Int, type: String) {
        var txnRequestBody = TransactionRequestModel()
        txnRequestBody.page = currentPage.toString()

        if (type.equals(Constants.ALL_STRING)) {
            getGramCashTxnJob.cancelIfActive()
            getNavigator()?.showLoaderFooter()
            getGramCashTxnJob = viewModelScope.launch {


                val allGramCashTxnResponse =
                    settingsRepository.getGramCashTxn(type, txnRequestBody)

                if (allGramCashTxnResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                    mAllTxnList.addAll(allGramCashTxnResponse.body()?.gpApiResponseData?.gramcashTxn as ArrayList<GramcashTxnItem?>)
                    allListSize.set(mAllTxnList.size)
                    val lastpage = allGramCashTxnResponse.body()?.gpApiResponseData?.lastPage
                    if (currentPage == lastpage) {
                        isLastPage = true

                    }
                    getNavigator()?.onListUpdated()
                } else {
                    getNavigator()?.onListUpdated()
                }

            }
        } else if (type.equals(Constants.REFERRAL)) {
            getGramCashTxnJob.cancelIfActive()
            getNavigator()?.showLoaderFooterReferral()
            getGramCashTxnJob = viewModelScope.launch {


                val referralGramCashResponse =
                    settingsRepository.getGramCashTxn(type, txnRequestBody)

                if (referralGramCashResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                    mReferralTxnList.addAll(referralGramCashResponse.body()?.gpApiResponseData?.gramcashTxn as ArrayList<GramcashTxnItem?>)
                    referralsListSize.set(mReferralTxnList.size)
                    val lastpage = referralGramCashResponse.body()?.gpApiResponseData?.lastPage
                    if (currentPage == lastpage) {
                        isLastPageReferral = true

                    }
                    getNavigator()?.onListUpdatedReferral()
                } else {
                    getNavigator()?.onListUpdatedReferral()
                }

            }


        }
    }


}