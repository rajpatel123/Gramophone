package agstack.gramophone.ui.home.product.fragment

import agstack.gramophone.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ProductImagesFragment :Fragment(){
    lateinit var  prodImagesFragmentListener: ProductImagesFragmentInterface
    override fun onAttach(context: Context) {
        super.onAttach(context)
        prodImagesFragmentListener = context as ProductImagesFragmentInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val prodImage = requireArguments()!!.getString(PRODUCT_IMAGE)
        val position = requireArguments()!!.getInt(POSITION)
        val v = inflater.inflate(R.layout.fragment_product_images, container, false)
        val imageView = v.findViewById<View>(R.id.product_image_view) as ImageView


        Glide.with(requireActivity())
            .load(prodImage)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)


        imageView.setOnClickListener {
            prodImagesFragmentListener.onItemClick(position)
        }


        return v
    }

    interface ProductImagesFragmentInterface {
        fun onItemClick(position: Int)
    }

    companion object {
        val PRODUCT_IMAGE = "PRODUCT_IMAGE"
        val POSITION = "POSITION"



        /*
        * Creating the new instance of fragment
         */
        fun newInstance(productImage: String, position: Int,onClickItem: (Int) -> Unit): ProductImagesFragment {
            val f = ProductImagesFragment()
            val bdl = Bundle(2)
            bdl.putString(PRODUCT_IMAGE, productImage)
            bdl.putInt(POSITION, position)
            f.arguments = bdl

           // selectedPosition = onClickItem
           // this.selectedPosition = onClickItem
            return f
        }
    }


}
