package agstack.gramophone.ui.verifyotp.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityVerifyOtpBinding
import agstack.gramophone.ui.dialog.LanguageBottomSheetFragment
import agstack.gramophone.ui.login.view.LoginActivity
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import agstack.gramophone.ui.verifyotp.viewmodel.VerifyOtpViewModel
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_verify_otp.*
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class VerifyOtpActivity :
    BaseActivityWrapper<ActivityVerifyOtpBinding, VerifyOTPNavigator, VerifyOtpViewModel>(),
    VerifyOTPNavigator, LanguageBottomSheetFragment.LanguageUpdateListener {
    private val verifyOtpViewModel: VerifyOtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyOtpViewModel.timeOver.set(true)
        updateUI()
    }

    private fun updateUI() {
        verifyOtpViewModel.updateMessage()
    }

    override fun getMobileBundle(): Bundle? = intent?.getBundleExtra(Constants.BUNDLE)

    override fun updateOTPView() {
        ivCall.setImageResource(R.drawable.ic_call_enabled)
        ivSMS.setImageResource(R.drawable.ic_sms)
        tvTime.visibility = View.INVISIBLE
        tvResend.visibility = View.INVISIBLE
        tvResendOtp.visibility = VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        openActivity(LoginActivity::class.java, Bundle().apply {
            putString(Constants.MOBILE_NO,verifyOtpViewModel.mobileNo.get())
        })
    }

    override fun getLayoutID(): Int = R.layout.activity_verify_otp

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): VerifyOtpViewModel = verifyOtpViewModel

    override fun getBundle(): Bundle? = intent.extras


    override fun showTimer(duration: Long) {
        tvTime.visibility = VISIBLE
        tvResend.visibility = VISIBLE
        tvResendOtp.visibility = GONE
        ivCall.setImageResource(R.drawable.ic_call)
        ivSMS.setImageResource(R.drawable.ic_sms_grey)
        object : CountDownTimer(duration, 1000) {
            override fun onTick(millis: Long) {
                verifyOtpViewModel.remaningDuration=millis
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
                tvTime.text = ms+getMessage(R.string.seconds)
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                verifyOtpViewModel.timeOver.set(true)
                verifyOtpViewModel.resendOTPType.set(getMessage(R.string.resend))
                verifyOtpViewModel.remaningDuration=0
                ivCall.setImageResource(R.drawable.ic_call_enabled)
                ivSMS.setImageResource(R.drawable.ic_sms)
                tvTime.visibility = View.INVISIBLE
                tvResend.visibility = View.INVISIBLE
                tvResendOtp.visibility = VISIBLE
            }
        }.start()
    }

    override fun onHelpClick(number: String) {
        verifyOtpViewModel.onHelpClick()
    }

    override fun onLanguageChangeClick() {
        val bottomSheet = LanguageBottomSheetFragment()
        bottomSheet.setLanguageListener(this)
        bottomSheet.show(
            getSupportFragmentManager(),
            getMessage(R.string.bottomsheet_tag)
        )
    }



    override fun onLanguageUpdate() {
        verifyOtpViewModel.updateLanguage()
    }


}