package agstack.gramophone.ui.userprofile.verifyotp

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseDialogFragment
import agstack.gramophone.databinding.VerifyOtpDialogBinding
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerifyOTPDialogFragment: BaseDialogFragment<VerifyOtpDialogBinding, VerifyOtpDialogNavigator, VerifyOtpDialogViewModel>() ,
    VerifyOtpDialogNavigator {
    var onCancelClick: (() -> Unit)? = null
    var mobileNoFromBundle :String=""
    private val verifyOtpDialogViewModel: VerifyOtpDialogViewModel by viewModels()




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel?.sendOTP()
    }



    override fun getLayoutID(): Int = R.layout.verify_otp_dialog

    override fun getBindingVariable(): Int = BR.viewModel


    override fun getViewModel(): VerifyOtpDialogViewModel {
       return verifyOtpDialogViewModel
    }

    companion object {


        val TAG: String?="VerifyOTPDialogFragment"

        fun newInstance(mobileNo:String): VerifyOTPDialogFragment {
            val fragment = VerifyOTPDialogFragment()

            return fragment
        }

    }




}