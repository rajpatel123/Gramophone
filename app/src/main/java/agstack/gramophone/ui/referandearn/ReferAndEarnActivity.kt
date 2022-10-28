package agstack.gramophone.ui.referandearn

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ReferEarnActivityBinding
import agstack.gramophone.utils.ShareSheetPresenter
import agstack.gramophone.utils.Utility
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.drawToBitmap
import com.google.zxing.qrcode.encoder.QRCode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferAndEarnActivity :
    BaseActivityWrapper<ReferEarnActivityBinding, ReferandEarnNavigator, ReferandEarnViewModel>(),
    ReferandEarnNavigator, ShareSheetPresenter.GenericUriHandler {

    private val referandEarnViewModel: ReferandEarnViewModel by viewModels()
    private var shareSheetPresenter: ShareSheetPresenter? = null
    var qrBitmap: Bitmap? = null
    var QRCodeURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.refer_n_earn), R.drawable.ic_arrow_left)
        mViewModel?.getGramCash()
        generateQRCode()

    }

    private fun generateQRCode() {
        shareSheetPresenter = this?.let { ShareSheetPresenter(this, it) }
        shareSheetPresenter!!.shareDynamicLink()
    }

    override fun getQRCodeURI(): String? {
        return QRCodeURI.toString()
    }


    override fun setQRCodeImage(bitmap: Bitmap?) {
        qrBitmap = bitmap
        QRCodeURI = Utility.bitmapToUri(this, qrBitmap)
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

        // mViewModel?.generateQrCode(genericUri.toString())


    }

    override fun share(currentShareOption: String, shareText: String?) {
        var shareMessage = resources.getString(R.string.welcome_msg)
        shareText?.let {
            shareMessage = shareText
        }


        var QRCodeURI: Uri? = Utility.bitmapToUri(this, qrBitmap)
        shareSheetPresenter?.shareDeepLinkWithExtraTextWithOption(
            shareMessage,
            getString(R.string.home_share_subject),
            QRCodeURI,
            currentShareOption
        )

        // var datainBundle


    }


    override fun shareReferalCode(
        currentShareOption: String,
        shareText: String?,
        referralCode: String?
    ) {
        var shareMessage = resources.getString(R.string.welcome_msg)


        referralCode?.let {
            shareMessage = "Your referal code is:"+ referralCode
        }

        shareText?.let {
            shareMessage = shareMessage +  shareText
        }


        var QRCodeURI: Uri? = Utility.bitmapToUri(this, qrBitmap)
        shareSheetPresenter?.shareDeepLinkWithExtraTextWithOption(
            shareMessage,
            getString(R.string.home_share_subject),
            QRCodeURI,
            currentShareOption
        )

    }


    override fun convertedReferralLayoutsBitmap() {
        Utility.saveImage(this, mViewModel?.QR_BitmapfromURL)


    }


    override fun onReferralCodeClick(referalCode:String) {
        val clipboard =this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("referral_code", referalCode)
        clipboard!!.setPrimaryClip(clip)
        Toast.makeText(this, getString(R.string.copied), Toast.LENGTH_SHORT).show();
    }

}