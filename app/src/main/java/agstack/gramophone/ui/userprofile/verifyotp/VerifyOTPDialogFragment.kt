package agstack.gramophone.ui.userprofile.verifyotp

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseDialogFragment
import agstack.gramophone.databinding.VerifyOtpDialogBinding
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.viewModels
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerifyOTPDialogFragment :
    BaseDialogFragment<VerifyOtpDialogBinding, VerifyOtpDialogNavigator, VerifyOtpDialogViewModel>(),
    VerifyOtpDialogNavigator {

    private val verifyOtpDialogViewModel: VerifyOtpDialogViewModel by viewModels()
    var update: ((String) -> Unit?)? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mobileNoFromBundle = requireArguments()!!.getString(Mobile_no)
        val otp_reference_id = requireArguments()!!.getInt(OTPReferenceID)
        mobileNoFromBundle?.let {
            mViewModel?.showInitialValues(mobileNoFromBundle,otp_reference_id)
        }

    }


    override fun getLayoutID(): Int = R.layout.verify_otp_dialog

    override fun getBindingVariable(): Int = BR.viewModel


    override fun getViewModel(): VerifyOtpDialogViewModel {
        return verifyOtpDialogViewModel
    }

    companion object {


        val TAG: String? = "VerifyOTPDialogFragment"
        val Mobile_no = "Mobile_no"
        val OTPReferenceID = "otp_reference_id"


        fun newInstance(mobileNo: String,otp_reference_id:Int): VerifyOTPDialogFragment {
            val fragment = VerifyOTPDialogFragment()
            val bdl = Bundle(2)
            bdl.putString(Mobile_no, mobileNo)
            bdl.putInt(OTPReferenceID,otp_reference_id)
            fragment.arguments = bdl

            return fragment
        }

    }

    override fun showTimer(duration: Long) {
        mViewModel?.timeOver?.set(false)
        binding.tvTime.visibility = View.VISIBLE
        binding.tvResend.visibility = View.VISIBLE
        binding.tvResendOtp.visibility = View.GONE
        binding.ivCall.setImageResource(R.drawable.ic_call)
        binding.ivSMS.setImageResource(R.drawable.ic_sms_grey)
        object : CountDownTimer(duration, 1000) {
            override fun onTick(millis: Long) {
                mViewModel?.remaningDuration = millis
                mViewModel?.calculateRemainigTime(millis)

            }

            override fun onFinish() {
                mViewModel?.timeOver?.set(true)
                mViewModel?.resendOTPType?.set(getMessage(R.string.resend))
                mViewModel?.remaningDuration = 0
                binding.ivCall.setImageResource(R.drawable.ic_call_enabled)
                binding.ivSMS.setImageResource(R.drawable.ic_sms)
                binding.tvTime.visibility = View.INVISIBLE
                binding.tvResend.visibility = View.INVISIBLE
                binding.tvResendOtp.visibility = View.VISIBLE
            }
        }.start()
    }

    override fun dismissDialogFragment(status:String?) {
        dismiss()
        if(status==Constants.GP_API_STATUS){
            update?.invoke(Constants.GP_API_STATUS)
        }
    }

    override fun finishActivity() {
    }


    fun setOnSuccessListener(update: (String) -> Unit) {
        this.update = update
    }

    override fun sendResendOtpMoEngageEvent() {
        val properties = Properties()
        properties.addAttribute("Customer_Mobile_Number", verifyOtpDialogViewModel.mobileNo.get())
            .addAttribute("Profile ID",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(requireContext(), "KA_resend_OTP", properties)
    }

    override fun sendIsOtpVerifiedMoEngageEvent(isOTPVerified: Boolean) {
        val properties = Properties()
        properties.addAttribute("Customer_Mobile_Number", verifyOtpDialogViewModel.mobileNo.get())
            .addAttribute("Profile ID",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        if (isOTPVerified) {
            MoEAnalyticsHelper.trackEvent(requireContext(), "KA_Edit_OTP_Verified", properties)
            MoEAnalyticsHelper.trackEvent(requireContext(), "KA_update", properties)
        } else {
            MoEAnalyticsHelper.trackEvent(requireContext(), "KA_OTP_Verification_Failed", properties)
        }
    }


}