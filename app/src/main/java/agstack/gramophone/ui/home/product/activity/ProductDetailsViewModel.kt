package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.product.model.ProductSkuOfferModel
import agstack.gramophone.ui.home.product.model.ProductWeightPriceModel
import agstack.gramophone.data.repository.onboarding.OnboardingRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.utils.Utility.toBulletedList
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel<ProductDetailsNavigator>() {
    var mSKUList = ArrayList<ProductWeightPriceModel>()

    var mSkuOfferList = ArrayList<ProductSkuOfferModel>()
    var productDetailsBulletText = ObservableField<String>()
    lateinit var productData: ProductData


    fun onAddToCartClicked() {
        productDetailsBulletText.set(listOf("One", "Two", "Three").toBulletedList().toString())
        Log.d("ProductDetailedList", productDetailsBulletText.toString())
        viewModelScope.launch {

/*
    val sendOTP = onboardingRepository.sendOTP(HashMap())*/
        }


    }

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.getParcelable<ProductData>("product") != null) {
            Log.d(
                "ProductName",
                (bundle?.getParcelable<ProductData>("product") as ProductData).product_name.toString()
            )
            productData = bundle?.getParcelable<ProductData>("product") as ProductData

            getNavigator()?.setToolbarTitle(productData?.product_name!!)

            mSKUList.add(ProductWeightPriceModel(1, "250", "799", "Gm", true))
            mSKUList.add(ProductWeightPriceModel(1, "500", "999", "Gm", false))
            mSKUList.add(ProductWeightPriceModel(1, "1", "799", "Kg", false))

            getNavigator()?.setProductSKUAdapter(ProductSKUAdapter(mSKUList)) {


            }
            mSkuOfferList.add(ProductSkuOfferModel(1, "1", "799", "Kg", false))
            mSkuOfferList.add(ProductSkuOfferModel(2, "1", "799", "Kg", false))
            getNavigator()?.setProductSKUOfferAdapter(ProductSKUOfferAdapter(mSkuOfferList)) {


            }

        }
    }
}