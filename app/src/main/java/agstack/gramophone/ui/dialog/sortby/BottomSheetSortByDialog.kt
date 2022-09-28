package agstack.gramophone.ui.dialog.sortby

import agstack.gramophone.databinding.BottomSheetDialogSortByBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetSortByDialog(
    private val sortDataList: ArrayList<SortByData>?,
    private val listener: (String) -> Unit,
) :
    BottomSheetDialogFragment() {
    var binding: BottomSheetDialogSortByBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BottomSheetDialogSortByBinding.inflate(inflater)

        setupUi()
        return binding!!.root
    }

    private fun setupUi() {
        binding?.rvSortBy?.adapter = SortByAdapter(requireContext(), sortDataList) {
            listener.invoke(it)
        }
        binding?.ivCloseDialog?.setOnClickListener {
            dismiss()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}