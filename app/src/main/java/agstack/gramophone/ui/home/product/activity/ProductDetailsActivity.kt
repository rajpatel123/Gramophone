package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.R
import agstack.gramophone.BR
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductDetailBinding
import agstack.gramophone.ui.home.product.fragment.ProductImagesFragment
import agstack.gramophone.ui.home.product.fragment.RelatedProductFragment
import agstack.gramophone.ui.home.product.fragment.RelatedProductFragmentAdapter
import agstack.gramophone.ui.home.product.model.ProductSkuOfferModel
import agstack.gramophone.ui.home.product.model.ProductWeightPriceModel
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.utils.Utility.toBulletedList
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_radio_product_packing.*
import kotlinx.android.synthetic.main.product_detail.view.*


@AndroidEntryPoint
class ProductDetailsActivity :
    BaseActivityWrapper<ProductDetailBinding, ProductDetailsNavigator, ProductDetailsViewModel>(),
    ProductDetailsNavigator , ProductImagesFragment.ProductImagesFragmentInterface {

    private lateinit var productDetails : ProductData
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    var productDetailsBulletText = ObservableField<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* binding = ProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)*/
        productDetailsViewModel.getBundleData()

        productDetailsViewModel.onAddToCartClicked()

        productDetailsViewModel?.apply {


            viewDataBinding?.productImagesViewPager?.adapter = ProductImagesAdapter(supportFragmentManager, productDetailsViewModel.productData.product_images!!)
            viewDataBinding?.dotsIndicator?.setViewPager(viewDataBinding!!.productImagesViewPager)
        }



        productDetailsBulletText.set(listOf("One", "Two", "Three").toBulletedList().toString())
        viewDataBinding?.tvProductDetails?.setText(productDetailsBulletText.get())
        initRelatedProducts()


    }


    private fun initRelatedProducts() {
        viewDataBinding?.rvRelatedProducts?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewDataBinding?.rvRelatedProducts?.setHasFixedSize(true)
        viewDataBinding?.rvRelatedProducts?.adapter = RelatedProductFragmentAdapter()


        /*var fragment= RelatedProductFragment()
        val bundle = Bundle()
        fragment.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentRelatedProduct, fragment)
            .commit()*/

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflator = menuInflater
        inflator.inflate(R.menu.menu_product_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cart -> {
                Log.d("Show cart clicked", "Show Cart")
            }

        }
        return true
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun getLayoutID(): Int {
        return R.layout.product_detail
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): ProductDetailsViewModel {
        return productDetailsViewModel
    }

    override fun setToolbarTitle(title: String?) {
        setUpToolBar(true, title!!, R.drawable.ic_arrow_left)
    }

    override fun setProductSKUAdapter(
        productSKUAdapter: ProductSKUAdapter,
        onSKUItemClicked: (ProductWeightPriceModel) -> Unit
    ) {

        productSKUAdapter.selectedProduct = onSKUItemClicked
        viewDataBinding?.rvProductSku?.adapter = productSKUAdapter

    }


    override fun setProductSKUOfferAdapter(
        productSKUOfferAdapter: ProductSKUOfferAdapter,
        onOfferItemClicked: (ProductSkuOfferModel) -> Unit
    ) {
        productSKUOfferAdapter.selectedProduct = onOfferItemClicked
        viewDataBinding?.rvAvailableoffers?.adapter = productSKUOfferAdapter
    }

    override fun onItemClick(position: Int) {
        Log.d("Position of item",position.toString())
    }
}
