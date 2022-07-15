package agstack.gramophone.ui.verifyotp.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityVerifyOtpBinding
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.ui.dialog.LanguageBottomSheetFragment
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.login.view.LoginActivity
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import agstack.gramophone.ui.verifyotp.viewmodel.VerifyOtpViewModel
import agstack.gramophone.utils.Constants.MOBILE_NO
import agstack.gramophone.utils.ApiResponse
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_verify_otp.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class VerifyOtpActivity : BaseActivityWrapper<ActivityVerifyOtpBinding, VerifyOTPNavigator, VerifyOtpViewModel>(),
    VerifyOTPNavigator, LanguageBottomSheetFragment.LanguageUpdateListener {
    private  val verifyOtpViewModel: VerifyOtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
        updateUI()

    }

    private fun updateUI() {
       verifyOtpViewModel.updateMessage()
    }

    private fun setupObservers() {
        verifyOtpViewModel.validateOtpResponseModel.observe(this, Observer{
            when (it) {
                is ApiResponse.Success -> {
                    Toast.makeText(this@VerifyOtpActivity,it.data?.gp_api_message, Toast.LENGTH_LONG).show()
                    val bundle = Bundle()
                    //Add your data from getFactualResults method to bundle
                    bundle.putString(MOBILE_NO, verifyOtpViewModel.mobileNo)
                    openAndFinishActivity(HomeActivity::class.java,bundle)
                }
                is ApiResponse.Error -> {
                    Toast.makeText(this@VerifyOtpActivity,it.message, Toast.LENGTH_LONG).show()

                }
                is ApiResponse.Loading -> {
                }
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        openActivity(LoginActivity::class.java,null)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_verify_otp
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): VerifyOtpViewModel {
      return verifyOtpViewModel
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun changeNumber() {
        openAndFinishActivity(LoginActivity::class.java,null)
    }

    override fun showTimer() {
        tvTime.visibility=VISIBLE
        tvResend.visibility=VISIBLE
        tvResendOtp.visibility= GONE
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millis: Long) {
                val ms = java.lang.String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(
                            millis
                        )
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            millis
                        )
                    )
                )
                tvTime.text =ms+" Sec"
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                tvTime.visibility=GONE
                tvResend.visibility=GONE
                tvResendOtp.visibility= VISIBLE
            }
        }.start()
    }

    override fun onError(message: String?) {
        TODO("Not yet implemented")
    }

    override fun onSuccess(message: String?) {
        Toast.makeText(this@VerifyOtpActivity,message,Toast.LENGTH_LONG).show()
    }

    override fun onLoading() {
        TODO("Not yet implemented")
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

    override fun onLanguageUpdate() {
        verifyOtpViewModel.updateLanguage()
    }
}