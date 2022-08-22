package agstack.gramophone.ui.dialog.cart

import agstack.gramophone.databinding.BottomSheetAddToCartBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddToCartBottomSheetDialog : BottomSheetDialogFragment() {
    lateinit var customerSupportNumber: String
    var binding: BottomSheetAddToCartBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAddToCartBinding.inflate(inflater)

        setupUi()
        return binding!!.root
    }

    private fun setupUi() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}