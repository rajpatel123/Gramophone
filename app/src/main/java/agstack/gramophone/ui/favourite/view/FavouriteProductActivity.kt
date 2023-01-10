package agstack.gramophone.ui.favourite.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityFavouriteProductBinding
import agstack.gramophone.databinding.ActivityFeaturedProductsBinding
import agstack.gramophone.databinding.ActivityFeaturedProductsBindingImpl
import agstack.gramophone.ui.favourite.FavouriteProductAdapter
import agstack.gramophone.ui.favourite.FavouriteProductNavigator
import agstack.gramophone.ui.favourite.model.Data
import agstack.gramophone.ui.favourite.viewmodel.FavouriteProductViewModel
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.moengage.core.Properties
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_community.*

@AndroidEntryPoint
class FavouriteProductActivity : BaseActivityWrapper<ActivityFavouriteProductBinding, FavouriteProductNavigator, FavouriteProductViewModel>(), FavouriteProductNavigator {

    private val favouriteProductViewModel : FavouriteProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(
            enableBackButton = true,
            getMessage(R.string.favourite_products),
            R.drawable.ic_arrow_left
        )
        favouriteProductViewModel.getFavouriteProduct()

        val properties = Properties()
        properties.addAttribute(
            "Customer_Id",
            SharedPreferencesHelper.instance?.getString(
                SharedPreferencesKeys.CUSTOMER_ID
            )!!
        )
            .setNonInteractive()
        sendMoEngageEvent(" KA_Click_ExploreProducts", properties)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_favourite_product
    }

    override fun getBindingVariable(): Int {
       return BR.viewModel
    }

    override fun getViewModel(): FavouriteProductViewModel {
       return favouriteProductViewModel
    }

    override fun updateProductList(
        favouriteProductAdapter: FavouriteProductAdapter,
        onProductClicked: (Data) -> Unit
    ) {
        favouriteProductAdapter.onProductClicked = onProductClicked
        viewDataBinding.rvProducts.setHasFixedSize(false)
        viewDataBinding.rvProducts.adapter = favouriteProductAdapter
    }
}