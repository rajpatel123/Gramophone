package agstack.gramophone.ui.dialog.sortby

import agstack.gramophone.databinding.BottomSheetDialogSortByBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetSortByDialog : BottomSheetDialogFragment() {
    var binding: BottomSheetDialogSortByBinding? = null
    var listener: AcceptRejectListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDialogSortByBinding.inflate(inflater)

        setupUi()
        return binding!!.root
    }

    private fun setupUi() {
        binding?.rvSortBy?.adapter = SortByAdapter(requireContext())
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