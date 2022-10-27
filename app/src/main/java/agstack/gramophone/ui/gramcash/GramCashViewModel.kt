package agstack.gramophone.ui.gramcash

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.faq.FAQAdapter
import agstack.gramophone.ui.gramcash.expiringsoon.GCExpiringSoonActivity
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.referandearn.ReferAndEarnActivity
import agstack.gramophone.ui.referandearn.model.GpApiResponseData
import agstack.gramophone.ui.referandearn.model.GramcashFaqItem
import agstack.gramophone.ui.referandearn.transaction.AllTransactionsListActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GramCashViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<GramCashNavigator>() {
    private var getGramCashJob: Job? = null
    var progressLoader = ObservableField<Boolean>(false)
    var gramCashResponseData = ObservableField<GpApiResponseData>()


    fun getGramCash() {
        getGramCashJob.cancelIfActive()
        getGramCashJob = checkNetworkThenRun {
            progressLoader.set(true)
            try{

                val gramCashResponsefromAPI = settingsRepository.getGramCash()

                if (gramCashResponsefromAPI.body()?.gpApiStatus!!.equals(Constants.GP_API_STATUS)) {
                    progressLoader.set(false)
                    val gramCashResponse: GpApiResponseData? = gramCashResponsefromAPI.body()?.gpApiResponseData
                    gramCashResponseData.set(gramCashResponse)
                    getNavigator()?.setFAQAdapter(FAQAdapter(gramCashResponse?.gramcashFaq as ArrayList<GramcashFaqItem>))


                    getNavigator()?.setGramCashRulesAdapter(FAQAdapter(gramCashResponse?.gramcashRules as ArrayList<GramcashFaqItem>))

                  //  getNavigator()?.showToast(gramCashResponsefromAPI.body()?.gpApiMessage)
                } else {
                    progressLoader.set(false)
                    getNavigator()?.showToast(gramCashResponsefromAPI.body()?.gpApiMessage)
                }
            }catch (e:Exception){
                Log.d("Exception",e.toString())
            }}
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



    fun viewAllTransactionClick() {
        getNavigator()?.openActivity(AllTransactionsListActivity::class.java)


    }

    fun manageaboutgramcash(){
        getNavigator()?.manageaboutBottomPopup()
    }

    fun OnClickGCexpiringin30days(){
        getNavigator()?.openActivity(GCExpiringSoonActivity::class.java, Bundle().apply {
            putString(Constants.GC_Expiring_soon,gramCashResponseData.get()?.gramcashExpiringSoon.toString())
        })
    }

    fun onRedeemNowClicked(){

        getNavigator()?.openAndFinishActivityWithClearTopNewTaskClearTaskFlags(HomeActivity::class.java, null)
    }

    fun onInviteNowClicked(){
        getNavigator()?.openAndFinishActivity(ReferAndEarnActivity::class.java)

    }

}