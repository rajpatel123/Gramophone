package agstack.gramophone.ui.home.product.fragment

import agstack.gramophone.databinding.ContactForPriceBinding
import agstack.gramophone.databinding.ExpertAdviceBottomsheetBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ContactForPriceBottomSheetDialog: BottomSheetDialogFragment() {
    var binding: ContactForPriceBinding? = null

    companion object {


        fun newInstance(): ContactForPriceBottomSheetDialog {
            val fragment = ContactForPriceBottomSheetDialog()
            return fragment
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContactForPriceBinding.inflate(inflater,container,false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()

    }


    private fun setupView() {
        binding?.icCross?.setOnClickListener {
            dismiss()
        }


    }



}