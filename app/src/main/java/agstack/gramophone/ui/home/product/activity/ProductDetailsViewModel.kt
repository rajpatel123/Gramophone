package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.product.ProductDetailsAdapter
import agstack.gramophone.ui.home.product.fragment.RelatedProductFragmentAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Utility.toBulletedList
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel<ProductDetailsNavigator>() {
    var mSKUList = ArrayList<ProductSkuListItem?>()

    var mSkuOfferList = ArrayList<OffersItem?>()

    var productData=ObservableField<GpApiResponseData?>()
    var productId: Int = 0
     lateinit var mProductDetailsKeyValues: MutableList<KeyPointsItem?>



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

                try {
                    val productDataResponse: Response<ProductDataResponse> =
                        productRepository.getProductData(productDetailstoBeFetched)
                    productData.set(productDataResponse.body()?.gpApiResponseData!!)
                    productData.let {
                        getNavigator()?.setToolbarTitle(productData?.get()?.productBaseName!!)

                        mProductDetailsKeyValues= (productData?.get()?.productDetails?.keyPoints!!).toMutableList()
                        /*for(i in 0..mProductDetailsKeyValues.size){
                            mProductDetailsKeyValues[i]?.viewType = Constants.NORMAL

                        }

                        mProductDetailsKeyValues.add(mProductDetailsKeyValues.size+1,
                            KeyPointsItem(null,null,Constants.FOOTER)
                        )*/
                        //set ProductDetails Adapter
                        getNavigator()?.setProductDetailsAdapter(ProductDetailsAdapter(ArrayList(mProductDetailsKeyValues)))


                        //set skuList
                        mSKUList = ArrayList(productData?.get()?.productSkuList)
                        mSKUList.let {
                            getNavigator()?.setProductSKUAdapter(ProductSKUAdapter(mSKUList)) {


                            }
                        }

                        //setOffer List
                        productData?.get()?.offers.let {
                            val prodOfferList = ArrayList(productData?.get()?.offers)
                            prodOfferList.let {
                                mSkuOfferList = ArrayList(prodOfferList)
                                getNavigator()?.setProductSKUOfferAdapter(
                                    ProductSKUOfferAdapter(
                                        mSkuOfferList
                                    )
                                ) {


                                }
                            }
                        }
                        productData?.get()?.productImages?.let {
                       getNavigator()?.setProductImagesViewPagerAdapter(ProductImagesAdapter(getNavigator()?.getFragmentManagerPager()!!,productData?.get()?.productImages!!))
                        }


                        productData?.get()?.relatedProduct?.let {
                            getNavigator()?.setRelatedProductsAdapter(RelatedProductFragmentAdapter(ArrayList(it))){

                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.toString())
                }
            }



        }
    }
}