package agstack.gramophone.widget

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FullImageAdapter(
    supportFragmentManager: FragmentManager,
    private val productImages: ArrayList<String>
) : FragmentPagerAdapter(supportFragmentManager){
    override fun getCount(): Int {
        return productImages.size
    }

    override fun getItem(position: Int): Fragment {
        return ViewZoomedOutImagesFragment.newInstance(productImages[position])
    }


}
