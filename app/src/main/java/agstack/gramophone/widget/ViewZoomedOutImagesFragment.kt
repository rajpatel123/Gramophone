package agstack.gramophone.widget

import agstack.gramophone.databinding.MultipleFullImageDetailBinding
import agstack.gramophone.ui.home.product.fragment.ProductImagesFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ViewZoomedOutImagesFragment : Fragment() {

    lateinit var mBinding: MultipleFullImageDetailBinding

    companion object {
        val PRODUCT_IMAGE = "PRODUCT_IMAGE"

        /*
        * Creating the new instance of fragment
         */
        fun newInstance(productImage: String): ViewZoomedOutImagesFragment {
            val f = ViewZoomedOutImagesFragment()
            val bdl = Bundle()
            bdl.putString(PRODUCT_IMAGE, productImage)
            f.arguments = bdl

            return f
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = MultipleFullImageDetailBinding.inflate(inflater, container, false)



        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prodImage = requireArguments().getString(ProductImagesFragment.PRODUCT_IMAGE)
        Glide.with(requireActivity())
            .load(prodImage)
            .into(mBinding.touchImageView)
    }
}