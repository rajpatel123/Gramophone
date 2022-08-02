package agstack.gramophone.ui.dialog

import agstack.gramophone.databinding.BottomSheetDialogHelpBinding
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetDialog : BottomSheetDialogFragment() {
    lateinit var customerSupportNumber: String
    var binding: BottomSheetDialogHelpBinding? = null
    var listener: AcceptRejectListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDialogHelpBinding.inflate(inflater)

        setupUi()
        return binding!!.root
    }

    private fun setupUi() {
        binding?.ivCloseDialog?.setOnClickListener {
            dismiss()
        }

        binding?.callMeNow?.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL);
            intent.data = Uri.parse("tel:$customerSupportNumber")
            startActivity(intent)
            dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun setAcceptRejectListener(listener: AcceptRejectListener?) {
        this.listener = listener
    }

    interface AcceptRejectListener {
        fun onAcceptRejectClick(friendRequestId: Int, status: Boolean)
    }
}