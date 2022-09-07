package agstack.gramophone.ui.userprofile.verifyotp

import agstack.gramophone.databinding.VerifyOtpDialogBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class VerifyOTPDialogFragment :DialogFragment() {
    var onCancelClick: (() -> Unit)? = null
    var binding: VerifyOtpDialogBinding? = null

    companion object {


        val TAG: String?="VerifyOTPDialogFragment"

        fun newInstance(mobileNo:String): VerifyOTPDialogFragment {
            val fragment = VerifyOTPDialogFragment()
            return fragment
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VerifyOtpDialogBinding.inflate(inflater)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // setupView(view)

    }

}