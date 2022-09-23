package agstack.gramophone.ui.dialog.filter

import agstack.gramophone.databinding.BottomSheetDialogFilterBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFilterDialog : BottomSheetDialogFragment() {
    var binding: BottomSheetDialogFilterBinding? = null
    var listener: AcceptRejectListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDialogFilterBinding.inflate(inflater)

        setupUi()
        return binding!!.root
    }

    private fun setupUi() {
        binding?.rvMainFilter?.adapter = MainFilterAdapter()
        binding?.rvSubFilter?.adapter = SubFilterAdapter()

        binding?.ivCloseDialog?.setOnClickListener {
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