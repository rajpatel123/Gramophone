package agstack.gramophone.ui.home.product

import agstack.gramophone.R
import agstack.gramophone.BR
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductDetailBinding
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailsActivity: BaseActivity<ProductDetailBinding, ProductDetailsNavigator, ProductDetailsViewModel>(),
    ProductDetailsNavigator{
    private lateinit var binding: ProductDetailBinding
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()




override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
    binding = ProductDetailBinding.inflate(layoutInflater)
    setContentView(binding.root)


}

    override fun getLayoutID(): Int {
        return R.layout.product_detail
    }

    override fun getBindingVariable(): Int {
    return BR.viewModel
    }

    override fun getViewModel():ProductDetailsViewModel {
       return productDetailsViewModel
    }
}
