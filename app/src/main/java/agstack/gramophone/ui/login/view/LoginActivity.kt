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
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity : BaseActivityWrapper<ActivityLoginBinding, LoginNavigator, LoginViewModel>(), LoginNavigator ,
    LanguageBottomSheetFragment.LanguageUpdateListener {

    //initialise ViewModel
    private val loginViewModel: LoginViewModel by viewModels()
    private var qrScan: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        loginViewModel.generateOtpResponseModel.observe(this, Observer{
            when (it) {
                is ApiResponse.Success -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this@LoginActivity,it.data?.gp_api_message,Toast.LENGTH_LONG).show()
                    val bundle = Bundle()
                    //Add your data from getFactualResults method to bundle
                    bundle.putString(Constants.MOBILE_NO, loginViewModel.mobileNo)
                    openAndFinishActivity(VerifyOtpActivity::class.java,bundle)
                }
                is ApiResponse.Error -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this@LoginActivity,it.message,Toast.LENGTH_LONG).show()
                }
                is ApiResponse.Loading -> {
                    progress.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun getLayoutID(): Int {
      return R.layout.activity_login
    }

    override fun getBindingVariable(): Int {
        return  BR.viewModel
    }

    override fun getViewModel(): LoginViewModel {
        return loginViewModel
    }

    override fun onHelpClick(number: String) {
        val bottomSheet = BottomSheetDialog()
        bottomSheet.customerSupportNumber= number
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

    override fun onSuccess(message: String?) {
        Toast.makeText(this@LoginActivity,message,Toast.LENGTH_LONG).show()
    }

    override fun openReferralDialog() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.activity_referral_dialog, null)
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
                tvCodeApplied.text="Referral Code "+etReferralCode.text.toString()+" Applied"
                loginViewModel.referralCode=etReferralCode.text.toString()
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
//            qrScan?.initiateScan()
        }

        tvTermsOfUse.setOnClickListener {
            loginViewModel.onTermsOfUseClicked()

        }
        //val etReferralCode =mDialogView.findViewById(R.id.etReferralCode) as EditText
        //login button click of custom layout
        mAlertDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_background);

    }

    override fun onLanguageUpdate() {
        loginViewModel.updateLanguage()
    }

}
