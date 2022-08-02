package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.product.ProductDetailsAdapter
import agstack.gramophone.ui.home.product.activity.productreview.AddEditProductReviewActivity
import agstack.gramophone.ui.home.product.fragment.GenuineCustomerRatingAlertFragment
import agstack.gramophone.ui.home.product.fragment.RelatedProductFragmentAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel<ProductDetailsNavigator>() {
    var mSKUList = ArrayList<ProductSkuListItem?>()

    var mSkuOfferList = ArrayList<PromotionListItem?>()

    var productData = ObservableField<GpApiResponseDataProduct?>()
    var productReviewsData = ObservableField<GpApiResponseData?>()
    var relatedProductData = ObservableField<GpApiResponseDataRelatedProduct?>()
    var productId: Int = 0
    lateinit var mProductDetailsKeyValues: MutableList<KeyPointsItem?>
    private var loadProductDataJob: Job? = null
    private var loadRelatedProductDataJob: Job? = null
    private var loadProductReviewsDataJob: Job? = null
    private var loadProductOffersDataJob: Job? = null
    private var addToCartJob: Job? = null
    var progressLoader = MutableLiveData<Boolean>()

    //Values selected by User
    var qtySelected = ObservableField<Int>(1)


    fun onAddQtyClicked() {
        qtySelected.set(qtySelected.get()!! + 1)
    }

    fun onMinusQtyClicked() {
        if (qtySelected.get()!! >= 2)
            qtySelected.set(qtySelected.get()!! - 1)
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

            loadProductDataJob?.takeIf { it.isActive }?.cancel()

            loadProductDataJob = viewModelScope.launch {

                //Start Loader


                val productDataResponse = productRepository
                    .getProductData(productDetailstoBeFetched)
                //stop loader
                if (productDataResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {


                    productData.set(productDataResponse.body()?.gpApiResponseData!!)
                    productData.let {
                        getNavigator()?.setToolbarTitle(productData?.get()?.productBaseName!!)
                        productData?.get()?.productImages?.let {
                            getNavigator()?.setProductImagesViewPagerAdapter(
                                ProductImagesAdapter(
                                    getNavigator()?.getFragmentManagerPager()!!,
                                    productData?.get()?.productImages!!
                                )
                            )
                        }

                        mProductDetailsKeyValues =
                            (productData?.get()?.productDetails?.keyPoints!!).toMutableList()

                        //set ProductDetails Adapter
                        getNavigator()?.setProductDetailsAdapter(
                            ProductDetailsAdapter(
                                ArrayList(
                                    mProductDetailsKeyValues
                                )
                            )
                        )


                        //set skuList
                        mSKUList = ArrayList(productData?.get()?.productSkuList)
                        mSKUList.let {
                            getNavigator()?.setProductSKUAdapter(
                                ProductSKUAdapter(
                                    mSKUList
                                )
                            ) {


                            }
                        }
                    }
                    loadRelatedProductData(productDetailstoBeFetched)
                } else {
                    //Loader should be cancelled
                }


            }


        }
    }

    private fun loadRelatedProductData(productDetailstoBeFetched: ProductData) {
        loadRelatedProductDataJob?.takeIf { it.isActive }?.cancel()
        loadRelatedProductDataJob = viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {


                    val relatedProductResponseDeferred = async {
                        productRepository.getRelatedProductsData(productDetailstoBeFetched)
                    }


                    val relatedProductResponse = relatedProductResponseDeferred.await()


                    if (relatedProductResponse.body()?.gpApiStatus.equals(
                            Constants.GP_API_STATUS
                        )
                    ) {
                        val responseData = relatedProductResponse.body()?.gpApiResponseData
                        relatedProductData.set(responseData)
                        responseData?.relatedProductList.let { relatedProductData ->
                            relatedProductData?.also {
                                getNavigator()?.setRelatedProductsAdapter(
                                    RelatedProductFragmentAdapter(
                                        it.filterNotNull()
                                    )
                                ) {
                                    //Open a new instance of ProductDetailsActivity with selected product ID
                                    getNavigator()?.openProductDetailsActivity(ProductData(it.productId!!))

                                }
                            }


                        }
                    }


                } else {
                    getNavigator()?.showToast(R.string.nointernet)
                }


            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }


        loadReviewData(productDetailstoBeFetched)
    }

    private fun loadReviewData(productDetailstoBeFetched: ProductData) {

        loadProductReviewsDataJob?.takeIf { it.isActive }?.cancel()
        loadProductReviewsDataJob = viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val productReviewResponse = productRepository.getProductReviewsData(
                        Constants.TOP,
                        null,
                        productDetailstoBeFetched
                    )

                    if (productReviewResponse != null && productReviewResponse?.body()?.gpApiStatus.equals(
                            Constants.GP_API_STATUS
                        )
                    ) {
                        productReviewsData.set(productReviewResponse.body()?.gpApiResponseData)
                        getNavigator()?.setRatingAndReviewsAdapter(
                            RatingAndReviewsAdapter(
                                productReviewsData.get()?.reviewList?.data as ArrayList<ReviewListItem?>,
                                2
                            )
                        )

                    }


                } else {
                    getNavigator()?.showToast(R.string.nointernet)
                }


            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
        loadOffersData(productDetailstoBeFetched)


    }

    private fun loadOffersData(productDetailstoBeFetched: ProductData) {
        loadProductOffersDataJob.cancelIfActive()
        loadProductOffersDataJob = checkNetworkThenRun {
            val offersOnProductResponse =
                productRepository.getOffersOnProductData(productDetailstoBeFetched)
            if (offersOnProductResponse.body()?.gpApiStatus.equals(
                    Constants.GP_API_STATUS
                )
            ) {
                //setOffer List
                offersOnProductResponse.body()?.gpApiResponseData?.offersProductList.let {
                    val prodOfferList =
                        ArrayList(offersOnProductResponse?.body()?.gpApiResponseData?.offersProductList)
                    prodOfferList.let {
                        mSkuOfferList = ArrayList(prodOfferList)
                        getNavigator()?.setProductSKUOfferAdapter(
                            ProductSKUOfferAdapter(mSkuOfferList),
                            {
                                //When RadioButton is clicked

                            },
                            {
                                //when view all is clicked
                                getNavigator()?.openActivity(
                                    OfferDetailActivity::class.java,
                                    Bundle().apply {
                                        putParcelable(Constants.OFFERSDATA, it)

                                    })
                            })
                    }
                }
            }
        }


    }

    private fun checkNetworkThenRun(runCode: (suspend () -> Unit)): Job {
        return viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    runCode.invoke()
                } else {
                    getNavigator()?.showToast(R.string.nointernet)
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }


    fun viewAllReviewsOnClick() {
        getNavigator()?.openViewAllReviewRatingsActivity(productId, productReviewsData.get())
    }


    fun openAddEditProductReview() {

        //If Genuine Customer
        getNavigator()?.openActivityWithBottomToTopAnimation(
            AddEditProductReviewActivity::class.java,
            Bundle().apply {

                putInt(Constants.Product_Id_Key, productId)
                putString(Constants.Product_Base_Name, productData.get()?.productBaseName)
                putParcelable(
                    Constants.PRODUCT_RATING_DATA_KEY,
                    productReviewsData.get()?.selfRating
                )
            })


        // else if not genuine customer

        getNavigator()?.showGenuineCustomerRatingDialog(GenuineCustomerRatingAlertFragment()) {
            //callback comes here when on add to cart is clicked
            Log.d("Click", "Add to cart Clicked")
            addToCartJob.cancelIfActive()
            addToCartJob = checkNetworkThenRun {
                progressLoader.value = true
               var  productDetailstoBeFetched= ProductData()
                val offersOnProductResponse =
                    productRepository.getOffersOnProductData(productDetailstoBeFetched)

                if (offersOnProductResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                    progressLoader.value = true
                }else{
                    progressLoader.value = true
                   // getNavigator()?.showToast(offersOnProductResponse.message())
                }
            }

        }

    }

}







