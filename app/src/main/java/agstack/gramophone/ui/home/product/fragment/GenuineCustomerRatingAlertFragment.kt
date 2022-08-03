package agstack.gramophone.ui.home.product.fragment

import agstack.gramophone.databinding.GenuineRatingDialogBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GenuineCustomerRatingAlertFragment : BottomSheetDialogFragment() {
    var binding: GenuineRatingDialogBinding? = null
    var onAddToCartClick: (() -> Unit)? = null

    companion object {


        fun newInstance(): GenuineCustomerRatingAlertFragment {
            val fragment = GenuineCustomerRatingAlertFragment()
            return fragment
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GenuineRatingDialogBinding.inflate(inflater)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)

    }


    private fun setupView(view: View) {
        binding?.icCross?.setOnClickListener {
            dismiss()
        }
        binding?.btnAddtocart?.setOnClickListener{
            onAddToCartClick?.invoke()
           // dismiss()
        }

    }

    fun setOnClickSelectedListener(onAddToCartClick: () -> Unit) {
        this.onAddToCartClick = onAddToCartClick

    }

}