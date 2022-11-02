package agstack.gramophone.ui.favourite.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityFeaturedProductsBinding
import agstack.gramophone.databinding.ActivityFeaturedProductsBindingImpl
import agstack.gramophone.ui.favourite.FavouriteProductNavigator
import agstack.gramophone.ui.favourite.viewmodel.FavouriteProductViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class FavouriteProductActivity : BaseActivityWrapper<ActivityFeaturedProductsBinding, FavouriteProductNavigator, FavouriteProductViewModel>(), FavouriteProductNavigator {

    private val favouriteProductViewModel : FavouriteProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_favourite_product
    }

    override fun getBindingVariable(): Int {
       return BR.viewModel
    }

    override fun getViewModel(): FavouriteProductViewModel {
        TODO("Not yet implemented")
    }
}