package agstack.gramophone.ui.home.product.fragment

import agstack.gramophone.R
import agstack.gramophone.databinding.GenuineRatingDialogBinding
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.amnix.xtension.extensions.enableIf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GenuineCustomerRatingAlertFragment : BottomSheetDialogFragment() {
    var binding: GenuineRatingDialogBinding? = null
    var onAddToCartClick: (() -> Unit)? = null
    var mContext :Context?=null

    companion object {
        val EnableAddToCart = "EnableAddToCart"

        fun newInstance(addtoCartEnabled: Boolean): GenuineCustomerRatingAlertFragment {
            val fragment = GenuineCustomerRatingAlertFragment()
            val bdl = Bundle(2)
            bdl.putBoolean(EnableAddToCart, addtoCartEnabled)
            fragment.arguments = bdl
            return fragment
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = GenuineRatingDialogBinding.inflate(inflater)
        mContext=inflater?.context

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addtoCartEnable = requireArguments().getBoolean(EnableAddToCart)
        binding?.btnAddtocart?.isEnabled = addtoCartEnable


        if (addtoCartEnable)
            binding?.btnAddtocart?.background = ContextCompat.getDrawable(mContext!!, R.drawable.active_button_background)
        else
            binding?.btnAddtocart?.background =  ContextCompat.getDrawable(mContext!!, R.drawable.deactive_button_background);

        setupView(view)

    }


    private fun setupView(view: View) {
        binding?.icCross?.setOnClickListener {
            dismiss()
        }
        binding?.btnAddtocart?.setOnClickListener {
            onAddToCartClick?.invoke()
            // dismiss()
        }

    }

    fun setOnClickSelectedListener(onAddToCartClick: () -> Unit) {
        this.onAddToCartClick = onAddToCartClick

    }

}