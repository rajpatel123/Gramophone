package agstack.gramophone.ui.referandearn

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.faq.FAQActivity
import agstack.gramophone.ui.referandearn.referralpoints.ReferralPointsActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.IntentKeys
import agstack.gramophone.utils.Utility.generateQR
import agstack.gramophone.utils.Utility.saveImage
import android.graphics.Bitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReferandEarnViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<ReferandEarnNavigator>() {
    private var currentShareOption=""
    var QR_BitmapfromURL: Bitmap? =null

    fun showReferralPointsActivity() {
        getNavigator()?.openActivity(ReferralPointsActivity::class.java)
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

}