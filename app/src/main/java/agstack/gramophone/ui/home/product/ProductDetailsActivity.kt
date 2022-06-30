package agstack.gramophone.ui.home.product

import agstack.gramophone.R
import agstack.gramophone.BR
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductDetailBinding
import agstack.gramophone.ui.home.product.model.ProductSkuOfferModel
import agstack.gramophone.ui.home.product.model.ProductWeightPriceModel
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_radio_product_packing.*


@AndroidEntryPoint
class ProductDetailsActivity :
    BaseActivityWrapper<ProductDetailBinding, ProductDetailsNavigator, ProductDetailsViewModel>(),
    ProductDetailsNavigator {
    private lateinit var binding: ProductDetailBinding
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productDetailsViewModel.getBundleData()


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
