package agstack.gramophone.ui.login.view


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityLoginBinding
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.ui.dialog.LanguageBottomSheetFragment
import agstack.gramophone.ui.login.LoginNavigator
import agstack.gramophone.ui.login.viewmodel.LoginViewModel
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import agstack.gramophone.ui.webview.view.WebViewActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.RESOLVE_HINT
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.api.GoogleApiClient
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity : BaseActivityWrapper<ActivityLoginBinding, LoginNavigator, LoginViewModel>(),
    LoginNavigator,
    LanguageBottomSheetFragment.LanguageUpdateListener, GoogleApiClient.ConnectionCallbacks {

    //initialise ViewModel
    private val loginViewModel: LoginViewModel by viewModels()
    private var qrScan: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestMobileNoHint()

    }


    override fun getLayoutID(): Int {
        return R.layout.activity_login
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): LoginViewModel {
        return loginViewModel
    }


    override fun onLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun onHelpClick(number: String) {
        val bottomSheet = BottomSheetDialog()
        bottomSheet.customerSupportNumber = number
        bottomSheet.show(
            getSupportFragmentManager(),
            "bottomSheet"
        )
    }

    override fun onLanguageChangeClick() {
        val bottomSheet = LanguageBottomSheetFragment()
        bottomSheet.setLanguageListener(this)
        bottomSheet.show(
            getSupportFragmentManager(),
            "bottomSheet"
        )
    }

    override fun openWebView(bundle: Bundle) {
        openActivity(WebViewActivity::class.java, bundle)
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

    override fun openReferralDialog() {
        //Inflate the dialog with custom view
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.activity_referral_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        //show dialog
        val mAlertDialog = mBuilder.show()
        val tvApplyCode = mDialogView.findViewById(R.id.tvApplyCode) as TextView
        val etReferralCode = mDialogView.findViewById(R.id.etReferralCode) as EditText
        val tvTermsOfUse = mDialogView.findViewById(R.id.tvTermsOfUse) as TextView
        val llQRLinearLayout = mDialogView.findViewById(R.id.llQRLinearLayout) as LinearLayout
        val llCrossLinearLayout = mDialogView.findViewById(R.id.llCrossLinearLayout) as LinearLayout
        tvApplyCode.setOnClickListener {
            if (!TextUtils.isEmpty(etReferralCode.text)) {
                tvCodeApplied.text = "Referral Code " + etReferralCode.text.toString() + " Applied"
                loginViewModel.referralCode = etReferralCode.text.toString()
                mAlertDialog.dismiss()
                rlHaveReferralCode.visibility = GONE
                rlAppliedCode.visibility = VISIBLE
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    getMessage(R.string.enter_referral_code),
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        llCrossLinearLayout.setOnClickListener {
            mAlertDialog.dismiss()
        }

        //having some crash while scanning will do it later as it is not mentioned in task description as well
        llQRLinearLayout.setOnClickListener {
//            qrScan = IntentIntegrator(this@LoginActivity)
//            qrScan?.setOrientationLocked(false)
            //qrScan?.initiateScan()
        }

        tvTermsOfUse.setOnClickListener {
            loginViewModel.onTermsOfUseClicked()

        }
        //val etReferralCode =mDialogView.findViewById(R.id.etReferralCode) as EditText
        //login button click of custom layout
        mAlertDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_background);

    }

    private fun requestMobileNoHint() {

        val googleApiClient = GoogleApiClient.Builder(this)
            .addApi(Auth.CREDENTIALS_API)
            .addConnectionCallbacks(this)
            .build()
        googleApiClient.connect()

        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val intent = Auth.CredentialsApi.getHintPickerIntent(
            googleApiClient, hintRequest
        )
        try {
            //Deprecation need to get alternative
            startIntentSenderForResult(
                intent.intentSender,
                Constants.RESOLVE_HINT, null, 0, 0, 0
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                val credential = data?.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
                // credential.getId(); <-- E.164 format phone number on 10.2.+ devices
                val mobileNo = credential?.id
                if (mobileNo != null && mobileNo.length >= 10) {
                    val finalMobileNo = mobileNo.substring(mobileNo.length - 10)
                    etMobile.setText(finalMobileNo)
                }
            }
        }
    }


    override fun onLanguageUpdate() {
        loginViewModel.updateLanguage()
    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

}
