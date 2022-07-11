package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.ui.home.product.fragment.ProductImagesFragment
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

internal class ProductImagesAdapter(
    supportFragmentManager: FragmentManager,
    private val productImages: List<String>
) : FragmentPagerAdapter(supportFragmentManager){



    override fun getCount(): Int {
        return this.productImages.size
    }

    /*
        * Passing the selected ProductImage to fragment
         */
    override fun getItem(position: Int): Fragment {
        return ProductImagesFragment.newInstance(productImages[position], position,){


        }
    }


}
