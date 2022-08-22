package agstack.gramophone.ui.login.view


import agstack.gramophone.BR
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
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity :
    BaseActivityWrapper<ActivityLoginBinding, LoginNavigator, LoginViewModel>(),
    LoginNavigator,
    LanguageBottomSheetFragment.LanguageUpdateListener {
    val REQUEST_CODE = 0x0000c0de
    var qrLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val resultData =
                    IntentIntegrator.parseActivityResult(REQUEST_CODE, result.resultCode, data)
                loginViewModel.setReferralCodeFromQR(resultData)


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

    override fun onHelpClick(number: String) {
        loginViewModel.onHelpClick()
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
        qrScan = IntentIntegrator(this@LoginActivity)
        qrScan?.setOrientationLocked(false)
        qrLauncher.launch(qrScan?.createScanIntent())
    }

}
