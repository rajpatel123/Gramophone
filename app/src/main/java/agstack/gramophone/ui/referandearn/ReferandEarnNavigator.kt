package agstack.gramophone.ui.referandearn

import agstack.gramophone.base.BaseNavigator
import android.graphics.Bitmap

interface ReferandEarnNavigator :BaseNavigator {
     fun setQRCodeImage(bitmap: Bitmap?)
     fun convertedReferralLayoutsBitmap()
     fun share(currentShareOption: String,shareText:String?)
     fun getQRCodeURI(): String?
      fun shareReferalCode(currentShareOption: String, shareText: String?, referralCode: String?)
}