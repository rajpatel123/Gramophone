package agstack.gramophone.ui.referandearn

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.faq.FAQActivity
import agstack.gramophone.ui.referandearn.referralpoints.ReferralPointsActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.IntentKeys
import agstack.gramophone.utils.Utility.generateQR
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import agstack.gramophone.ui.referandearn.model.GpApiResponseData
import agstack.gramophone.ui.referandearn.model.GramcashFaqItem
import agstack.gramophone.ui.referralrules.ReferralRulesActivity

@HiltViewModel
class ReferandEarnViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<ReferandEarnNavigator>() {
    private var currentShareOption=""
    var QR_BitmapfromURL: Bitmap? =null
    private var getGramCashJob: Job? = null
    var progressLoader = ObservableField<Boolean>(false)
    var gramCashResponseData = ObservableField<GpApiResponseData>()

    fun showReferralPointsActivity() {
        getNavigator()?.openActivity(ReferralPointsActivity::class.java, Bundle().apply {
            putParcelable(Constants.GramCashResponse,gramCashResponseData.get())
            putString(Constants.SHAREIMAGEURIStRING,getNavigator()?.getQRCodeURI())

        })
    }

    fun onFAQClicked() {
        getNavigator()?.openActivity(FAQActivity::class.java,Bundle().apply {
            putParcelableArrayList(Constants.GramCashFAQList,gramCashResponseData.get()?.gramcashFaq as ArrayList<GramcashFaqItem>)
        })
    }

    fun onReferralRulesClicked(){
        getNavigator()?.openActivity(ReferralRulesActivity::class.java,Bundle().apply {
            putStringArrayList(Constants.GramCashReferralRulesList,gramCashResponseData.get()?.referralRules as ArrayList<String>)
        })

    }

    fun generateQrCode(extraText: String) {
        var contentText = extraText

        if(gramCashResponseData.get()?.referral_code!=null){
            contentText = gramCashResponseData.get()?.referral_code!!
        }

        QR_BitmapfromURL= generateQR(contentText, 512)
        getNavigator()?.setQRCodeImage(QR_BitmapfromURL)


    }

    fun onDownloadQRClick() {

        getNavigator()?.convertedReferralLayoutsBitmap()


    }

    fun onShareClick(){
        currentShareOption = IntentKeys.OtherShareKey
        getNavigator()?.share(currentShareOption,gramCashResponseData.get()?.share_message)
    }

    fun onShareReferalClick(){
        currentShareOption = IntentKeys.OtherShareKey
        getNavigator()?.shareReferalCode(currentShareOption,gramCashResponseData.get()?.share_message,gramCashResponseData.get()?.referral_code)
    }

    fun onReferralCodeClick(){
getNavigator()?.onReferralCodeClick(gramCashResponseData.get()?.referral_code!!)
    }

    fun onWhatsappShareClick(){
        currentShareOption = IntentKeys.WhatsAppShareKey
        getNavigator()?.share(currentShareOption,gramCashResponseData.get()?.share_message)
    }

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
                generateQrCode(gramCashResponsefromAPI.body()?.gpApiResponseData?.referral_code!!)

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

}