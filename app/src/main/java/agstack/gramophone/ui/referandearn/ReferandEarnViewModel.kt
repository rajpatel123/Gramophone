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
        })
    }

    fun onFAQClicked() {
        getNavigator()?.openActivity(FAQActivity::class.java)
    }

    fun generateQrCode(extraText: String) {

        QR_BitmapfromURL= generateQR(extraText, 512)
        getNavigator()?.setQRCodeImage(QR_BitmapfromURL)


    }

    fun onDownloadQRClick() {

        getNavigator()?.convertedReferralLayoutsBitmap()


    }

    fun onShareClick(){
        currentShareOption = IntentKeys.OtherShareKey
        getNavigator()?.share(currentShareOption)
    }

    fun onWhatsappShareClick(){
        currentShareOption = IntentKeys.WhatsAppShareKey
        getNavigator()?.share(currentShareOption)
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

                getNavigator()?.showToast(gramCashResponsefromAPI.body()?.gpApiMessage)
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