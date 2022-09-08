package agstack.gramophone.ui.userprofile.verifyotp

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseDialogFragment
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.FragmentCommunityBinding
import agstack.gramophone.databinding.VerifyOtpDialogBinding
import agstack.gramophone.ui.home.view.fragments.CommunityFragmentNavigator
import agstack.gramophone.ui.home.view.fragments.community.viewmodel.CommunityViewModel
import agstack.gramophone.utils.ShareSheetPresenter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerifyOTPDialogFragment: BaseDialogFragment<VerifyOtpDialogBinding, VerifyOtpDialogNavigator, VerifyOtpDialogViewModel>() ,
    VerifyOtpDialogNavigator {
    var onCancelClick: (() -> Unit)? = null
    private val verifyOtpDialogViewModel: VerifyOtpDialogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun getLayoutID(): Int = R.layout.verify_otp_dialog

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): VerifyOtpDialogViewModel = verifyOtpDialogViewModel

    companion object {


        val TAG: String?="VerifyOTPDialogFragment"

        fun newInstance(mobileNo:String): VerifyOTPDialogFragment {
            val fragment = VerifyOTPDialogFragment()
            return fragment
        }

    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  VerifyOtpDialogBinding = VerifyOtpDialogBinding.inflate(inflater, container, false)


}