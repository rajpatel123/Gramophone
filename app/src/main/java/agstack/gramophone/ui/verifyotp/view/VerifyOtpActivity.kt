package agstack.gramophone.ui.verifyotp.view

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityVerifyOtpBinding
import agstack.gramophone.ui.dialog.LanguageBottomSheetFragment
import agstack.gramophone.ui.language.model.DeviceDetails
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.login.view.LoginActivity
import agstack.gramophone.ui.login.view.SmsBroadcastReceiver
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import agstack.gramophone.ui.verifyotp.viewmodel.VerifyOtpViewModel
import agstack.gramophone.utils.Constants
import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_verify_otp.*
import java.util.regex.Pattern


@AndroidEntryPoint
class VerifyOtpActivity :
    BaseActivityWrapper<ActivityVerifyOtpBinding, VerifyOTPNavigator, VerifyOtpViewModel>(),
    VerifyOTPNavigator, LanguageBottomSheetFragment.LanguageUpdateListener {
    private val verifyOtpViewModel: VerifyOtpViewModel by viewModels()
    val initiateAppDataRequestModel: InitiateAppDataRequestModel
        get() {
            val android_id = Settings.Secure.getString(
                this.contentResolver,
                Settings.Secure.ANDROID_ID
            )

            var deviceDetails = DeviceDetails(
                BuildConfig.VERSION_CODE.toString(),
                BuildConfig.VERSION_NAME,
                android_id,
                Build.MODEL,
                Build.VERSION.SDK_INT.toString()
            )
            var registerDeviceRequestModel = InitiateAppDataRequestModel(
                deviceDetails,
                getLanguage()
            )
            return registerDeviceRequestModel

        }

    var oTPReaderLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val message = data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                getOtpFromMessage(message!!)
            }

        }
    private var intentFilter: IntentFilter? = null
    private val REQ_USER_CONSENT = 200
    var smsBroadcastReceiver: SmsBroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyOtpViewModel.timeOver.set(true)
        startSmartUserConsent()
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        val properties = Properties()
        properties.addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_OTP_Verified_Successfully", properties)
    }

    private fun startSmartUserConsent() {
        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }


    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    oTPReaderLauncher.launch(intent)
                }

                override fun onFailure() {}
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    private fun getOtpFromMessage(message: String) {
        val otpPattern = Pattern.compile("(|^)\\d{6}")
        val matcher = otpPattern.matcher(message)
        if (matcher.find()) {
            verifyOtpViewModel.otp.set(matcher.group(0))
            verifyOtpViewModel.submitOtp()
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
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

    override fun getInitModel(): InitiateAppDataRequestModel = initiateAppDataRequestModel


    override fun onBackPressed() {
        super.onBackPressed()
        openActivity(LoginActivity::class.java, Bundle().apply {
            putString(Constants.MOBILE_NO, verifyOtpViewModel.mobileNo.get())
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
                verifyOtpViewModel.remaningDuration = millis
                verifyOtpViewModel.calculateRemainigTime(millis)
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                verifyOtpViewModel.timeOver.set(true)
                verifyOtpViewModel.resendOTPType.set(getMessage(R.string.resend))
                verifyOtpViewModel.remaningDuration = 0
                ivCall.setImageResource(R.drawable.ic_call_enabled)
                ivSMS.setImageResource(R.drawable.ic_sms)
                tvTime.visibility = View.INVISIBLE
                tvResend.visibility = View.INVISIBLE
                tvResendOtp.visibility = VISIBLE
            }
        }.start()
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

    override fun sendLanguageUpdateMoEngageEvent() {
        val properties = Properties()
        properties.addAttribute("Language", getLanguage())
            .addAttribute("Source_Screen", "Verify OTP")
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Language_Updated", properties)
    }

    override fun sendChangeMobileNoMoEngageEvent() {
        val properties = Properties()
        properties
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Change_Mobile_Number", properties)
    }

    override fun sendResendOTPMoEngageEvent(mobileNo: String) {
        val properties = Properties()
        properties
            .addAttribute("Customer_Mobile_Number", mobileNo)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_OTP_Resend", properties)
    }

    override fun sendVerifiedOTPMoEngageEvent(mobileNo: String, referralCode: String) {
        val properties = Properties()
        properties
            .addAttribute("Customer_Mobile_Number", mobileNo)
            .addAttribute("Referral_Code", referralCode)
            .addAttribute("Customer_Acquisition_Source", "ANDROID")
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_OTP_Verified_Successfully\n" +
                "\n", properties)
    }
}