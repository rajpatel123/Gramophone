package agstack.gramophone.ui.referandearn

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ReferEarnActivityBinding
import agstack.gramophone.utils.ShareSheetPresenter
import agstack.gramophone.utils.Utility
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.drawToBitmap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferAndEarnActivity :
    BaseActivityWrapper<ReferEarnActivityBinding, ReferandEarnNavigator, ReferandEarnViewModel>(),
    ReferandEarnNavigator, ShareSheetPresenter.GenericUriHandler {

    private val referandEarnViewModel: ReferandEarnViewModel by viewModels()
    private var shareSheetPresenter: ShareSheetPresenter? = null
    var qrBitmap: Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.refer_n_earn), R.drawable.ic_arrow_left)
        generateQRCode()
    }

    private fun generateQRCode() {
        shareSheetPresenter = this?.let { ShareSheetPresenter(this,it) }
        shareSheetPresenter!!.shareDynamicLink()
    }


    override fun setQRCodeImage(bitmap: Bitmap?) {
        qrBitmap = bitmap
        viewDataBinding?.shareyourreferal.referralCodeImageView?.setImageBitmap(bitmap)

    }

    override fun getLayoutID(): Int {
        return R.layout.refer_earn_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): ReferandEarnViewModel {
        return referandEarnViewModel
    }

    override fun processGenericUri(genericUri: Uri) {
        //temporarily share message = "Welcome to Gramophone"
        mViewModel?.generateQrCode(genericUri.toString())



    }

    override fun share(currentShareOption:String){
        val shareMessage = resources.getString(R.string.welcome_msg)
      //  val extraText = shareMessage + "\n" + genericUri.toString()
        val extraText = shareMessage

        var QRCodeURI :Uri? = Utility.bitmapToUri(this,qrBitmap)
        shareSheetPresenter?.shareDeepLinkWithExtraTextWithOption(extraText, getString(R.string.home_share_subject),QRCodeURI ,currentShareOption)


    }




    override fun convertedReferralLayoutsBitmap() {
        Utility.saveImage(this, mViewModel?.QR_BitmapfromURL)


    }

}