package agstack.gramophone.ui.login.view


import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityLoginBinding
import agstack.gramophone.databinding.ActivityReferralDialogBinding
import agstack.gramophone.ui.dialog.LanguageBottomSheetFragment
import agstack.gramophone.ui.login.LoginNavigator
import agstack.gramophone.ui.login.viewmodel.LoginViewModel
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import agstack.gramophone.ui.webview.view.WebViewActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.ImagePicker
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.amnix.xtension.extensions.isNotNull
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.qrcode.QRCodeReader
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*


@AndroidEntryPoint
class LoginActivity : BaseActivityWrapper<ActivityLoginBinding, LoginNavigator, LoginViewModel>(),
    LoginNavigator, LanguageBottomSheetFragment.LanguageUpdateListener,
    BottomSheetDialogScanQR.OnClickEvents {
    val REQUEST_CODE = 0x0000c0de
    var qrLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val resultData =
                    IntentIntegrator.parseActivityResult(REQUEST_CODE, result.resultCode, data)
                loginViewModel.setReferralCodeFromQR(referralCodeValue = resultData.contents)


            }
        }

    private val request: GetPhoneNumberHintIntentRequest =
        GetPhoneNumberHintIntentRequest.builder().build()

    private val phoneNumberLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            try {
                val phoneNumber =
                    Identity.getSignInClient(this).getPhoneNumberFromIntent(result.data)
                if (phoneNumber != null && phoneNumber.length >= 10) {
                    val finalMobileNo = phoneNumber.substring(phoneNumber.length - 10)
                    etMobile.setText(finalMobileNo)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    //initialise ViewModel
    private val loginViewModel: LoginViewModel by viewModels()
    private var qrScan: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel.setMobileNumber()
    }

    override fun getLayoutID(): Int = R.layout.activity_login

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): LoginViewModel = loginViewModel


    override fun onLoading() {
        progress.visibility = VISIBLE
    }

    override fun hideProgressBar() {
        progress.visibility = GONE

    }

    override fun onLanguageChangeClick() {
        val bottomSheet = LanguageBottomSheetFragment()
        bottomSheet.setLanguageListener(this)
        bottomSheet.show(
            getSupportFragmentManager(),
            getMessage(R.string.bottomsheet_tag)
        )
    }

    override fun openWebView(bundle: Bundle) {
        val properties = Properties()
        properties.addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_View_Terms_of_Service", properties)
        openActivity(WebViewActivity::class.java, bundle)
    }

    override fun getBundle(): Bundle? = intent?.extras
    override fun getMobileBundle(): Bundle? = intent?.getBundleExtra(Constants.BUNDLE)
    override fun showMobileNumberHint() {
        requestMobileNoHint()
    }


    override fun referralCodeRemoved() {
        rlHaveReferralCode.visibility = VISIBLE
        rlAppliedCode.visibility = GONE
    }

    override fun moveToNext(bundle: Bundle) {
        progress.visibility = View.GONE
        openAndFinishActivity(VerifyOtpActivity::class.java, bundle)
    }

    override fun onSuccess(message: String?) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun onError(message: String?) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun openReferralDialog() {
        //Inflate the dialog with custom view   use Binding
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.activity_referral_dialog, null)
        val dialogBinding = ActivityReferralDialogBinding.bind(mDialogView)
        dialogBinding.setVariable(BR.viewModel, loginViewModel)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
        //show dialog
        val mAlertDialog = mBuilder.show()
        loginViewModel.setDialog(mAlertDialog)

        mAlertDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_background);

    }

    private fun requestMobileNoHint() {
        Identity.getSignInClient(this)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener { result ->
                try {
                    phoneNumberLauncher.launch(IntentSenderRequest.Builder(result).build())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .addOnFailureListener {
                Log.d("LoginActivity", getMessage(R.string.unable_to_get_number))
            }

    }

    override fun onLanguageUpdate() {
        loginViewModel.updateLanguage()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState

    }

    override fun scanQR() {
        val bottomSheet = BottomSheetDialogScanQR()
        bottomSheet.setOnClickEventsListener(this)
        bottomSheet.show(
            supportFragmentManager,
            Constants.BOTTOM_SHEET
        )


    }

    override fun sendLanguageUpdateMoEngageEvent() {
        val properties = Properties()
        properties.addAttribute("Language", getLanguage())
            .addAttribute("Source_Screen", "Login")
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Language_Updated", properties)
    }

    override fun sendTermsMoEngageEvent() {
        val properties = Properties()
        properties
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_View_Terms_of_Service", properties)
    }

    override fun sendPrivacyMoEngageEvent() {
        val properties = Properties()
        properties
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_View_Privacy_Policy", properties)
    }

    override fun sendOtpSentMoEngageEvent(mobileNo: String) {
        val properties = Properties()
        properties.addAttribute("Customer_Mobile_Number", mobileNo)
            .addAttribute(
                "Referral_Code", if (loginViewModel.referralCodeValue.isNotNull()) {
                    loginViewModel.referralCodeValue
                } else {
                    ""
                }
            )
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Login_OTP_Sent", properties)
    }


    fun decodeQRImage(bMap: Bitmap): String? {
        var decoded: String? = null
        val intArray = IntArray(bMap.getWidth() * bMap.getHeight())
        bMap.getPixels(
            intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(),
            bMap.getHeight()
        )
        val source: LuminanceSource = RGBLuminanceSource(
            bMap.getWidth(),
            bMap.getHeight(), intArray
        )
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        val reader: Reader = QRCodeReader()
        try {
            val result = reader.decode(bitmap)
            decoded = result.getText()
        } catch (e: NotFoundException) {
            e.printStackTrace()
        } catch (e: ChecksumException) {
            e.printStackTrace()
        } catch (e: FormatException) {
            e.printStackTrace()
        }
        if (!TextUtils.isEmpty(decoded)) {
            loginViewModel.setReferralCodeFromQR(decoded!!)
        } else {
            Toast.makeText(this@LoginActivity, getString(R.string.invalid_qr), Toast.LENGTH_LONG)
                .show()
        }

        return decoded
    }

    override fun onCameraCLick() {
        qrScan = IntentIntegrator(this@LoginActivity)
        qrScan?.setOrientationLocked(false)
//        qrScan?.setBarcodeImageEnabled(true)
        qrScan?.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
        qrScan?.setBeepEnabled(true)
        qrLauncher.launch(qrScan?.createScanIntent())
    }

    override fun onGallaryClick() {
        val cameraIntent = ImagePicker.getGallaryIntent(this)
        gallaryIntentLauncher.launch(cameraIntent)
    }


    var gallaryIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                val imageUri = ImagePicker.getImageFromResult(this, result.resultCode, data)
                if (imageUri != null) {
                    val bitmap = ImagePicker.getImageResized(this, imageUri!!)
                    bitmap?.let { decodeQRImage(bitmap) }
                }else{
                    showToast(getMessage(R.string.invalid_qr))
                }


            }
        }

}
