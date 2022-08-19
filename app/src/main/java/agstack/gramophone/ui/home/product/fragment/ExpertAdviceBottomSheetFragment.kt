package agstack.gramophone.ui.home.product.fragment

import agstack.gramophone.databinding.ExpertAdviceBottomsheetBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ExpertAdviceBottomSheetFragment: BottomSheetDialogFragment() {
    var binding: ExpertAdviceBottomsheetBinding? = null
    var onCancelClick: (() -> Unit)? = null
    var onOkClick: (() -> Unit)? = null

    companion object {


        fun newInstance(): ExpertAdviceBottomSheetFragment {
            val fragment = ExpertAdviceBottomSheetFragment()
            return fragment
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ExpertAdviceBottomsheetBinding.inflate(inflater)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)

    }


    private fun setupView(view: View) {
        binding?.btnCancel?.setOnClickListener{
            onCancelClick?.invoke()

        }
        binding?.btnOk?.setOnClickListener{
            onOkClick?.invoke()

        }

    }

    fun setOnClickSelectedListener(onCancelClick: () -> Unit,onOkClick: () -> Unit) {
        this.onCancelClick = onCancelClick
        this.onOkClick = onOkClick

    }

}