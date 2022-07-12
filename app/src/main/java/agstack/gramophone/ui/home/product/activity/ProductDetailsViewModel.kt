package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.utils.Utility.toBulletedList
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel<ProductDetailsNavigator>() {
    var mSKUList = ArrayList<ProductSkuListItem?>()

    var mSkuOfferList = ArrayList<ProductSkuOfferItem?>()
    var productDetailsBulletText = ObservableField<String>()
    lateinit var productData: GpApiResponseData
    var productId: Int = 0


    fun onAddToCartClicked() {
        productDetailsBulletText.set(listOf("One", "Two", "Three").toBulletedList().toString())
        Log.d("ProductDetailedList", productDetailsBulletText.toString())


    }

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.getParcelable<ProductData>("product") != null) {
            Log.d(
                "ProductName",
                (bundle?.getParcelable<ProductData>("product") as ProductData).product_id.toString()
            )

            productId = (bundle?.getParcelable<ProductData>("product") as ProductData).product_id
            val productDetailstoBeFetched = ProductData()
            productDetailstoBeFetched.product_id = productId


            viewModelScope.launch {

                val productData1: Response<ProductDataResponse> =
                    productRepository.getProductData(productDetailstoBeFetched)
                productData = productData1.body()?.gpApiResponseData!!
            }

            getNavigator()?.setToolbarTitle(productData?.productName!!)
            val prodSKUList = productData?.productSkuList
            prodSKUList?.let {
                mSKUList = ArrayList(prodSKUList)
            }

            /* mSKUList.add(ProductWeightPriceModel(1, "250", "799", "Gm", true))
             mSKUList.add(ProductWeightPriceModel(1, "500", "999", "Gm", false))
             mSKUList.add(ProductWeightPriceModel(1, "1", "799", "Kg", false))*/

            getNavigator()?.setProductSKUAdapter(ProductSKUAdapter(mSKUList)) {


            }

            val prodOfferList = ArrayList(productData?.offers)
            prodOfferList.let {
                mSkuOfferList = ArrayList(prodOfferList)
            }
            /*mSkuOfferList.add(ProductSkuOfferModel(1, "1", "799", "Kg", false))
            mSkuOfferList.add(ProductSkuOfferModel(2, "1", "799", "Kg", false))*/
            getNavigator()?.setProductSKUOfferAdapter(ProductSKUOfferAdapter(mSkuOfferList)) {


            }

        }
    }
}