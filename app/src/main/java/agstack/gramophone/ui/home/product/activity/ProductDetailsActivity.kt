package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.R
import agstack.gramophone.BR
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductDetailBinding
import agstack.gramophone.ui.home.product.fragment.RelatedProductFragment
import agstack.gramophone.ui.home.product.fragment.RelatedProductFragmentAdapter
import agstack.gramophone.ui.home.product.model.ProductSkuOfferModel
import agstack.gramophone.ui.home.product.model.ProductWeightPriceModel
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


@AndroidEntryPoint
class ProductDetailsActivity :
    BaseActivityWrapper<ProductDetailBinding, ProductDetailsNavigator, ProductDetailsViewModel>(),
    ProductDetailsNavigator {
    private lateinit var binding: ProductDetailBinding
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    var productDetailsBulletText = ObservableField<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productDetailsViewModel.getBundleData()
        productDetailsViewModel.onAddToCartClicked()
        productDetailsBulletText.set(  listOf("One", "Two", "Three").toBulletedList().toString())
        binding.tvProductDetails.setText(productDetailsBulletText.get())
        initRelatedProducts()


    }

    private fun initRelatedProducts() {
        binding?.rvRelatedProducts?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvRelatedProducts?.setHasFixedSize(true)
        binding.rvRelatedProducts.adapter= RelatedProductFragmentAdapter()


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
        binding.rvProductSku.adapter = productSKUAdapter

    }


    override fun setProductSKUOfferAdapter(
        productSKUOfferAdapter: ProductSKUOfferAdapter,
        onOfferItemClicked: (ProductSkuOfferModel) -> Unit
    ) {
        productSKUOfferAdapter.selectedProduct = onOfferItemClicked
        binding.rvAvailableoffers.adapter = productSKUOfferAdapter
    }
}
