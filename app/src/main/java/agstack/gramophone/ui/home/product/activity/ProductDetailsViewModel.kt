package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.home.product.ProductDetailsAdapter
import agstack.gramophone.ui.home.product.activity.productreview.AddEditProductReviewActivity
import agstack.gramophone.ui.home.product.fragment.ContactForPriceBottomSheetDialog
import agstack.gramophone.ui.home.product.fragment.ExpertAdviceBottomSheetFragment
import agstack.gramophone.ui.home.product.fragment.GenuineCustomerRatingAlertFragment
import agstack.gramophone.ui.home.product.fragment.RelatedProductFragmentAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel<ProductDetailsNavigator>() {
    val productDetailstoBeFetched = ProductData()
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
    private var expertAdviceJob: Job? = null
    private var updateProductFavoriteJob: Job? = null
    private var contactForPriceJob :Job?=null
    var progressLoader = ObservableField<Boolean>(false)

    //Values selected by User
    var qtySelected = ObservableField<Int>(1)

    var ratingSelected = ObservableField<Double>(0.0)
    var isHeartSelected = ObservableField<Boolean>(false)
    var selectedSkuListItem = ObservableField<ProductSkuListItem>()
    var selectedOfferItem = PromotionListItem()
    var addToCartEnabled = ObservableField<Boolean>(true)
    fun onHeartIconClicked() {
        isHeartSelected.set(!isHeartSelected.get()!!)
        updateProductFavoriteJob.cancelIfActive()
        updateProductFavoriteJob = checkNetworkThenRun {
            progressLoader.set(true)
            var producttoBeAdded = ProductData()
            producttoBeAdded.product_id = productId
            producttoBeAdded.is_favourite = isHeartSelected.get()
            val updateProductFavJobResponse =
                productRepository.updateProductFavorite(producttoBeAdded)
            progressLoader.set(false)

            if (updateProductFavJobResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                getNavigator()?.showToast(updateProductFavJobResponse.body()?.gp_api_message)
            } else {
                getNavigator()?.showToast(updateProductFavJobResponse.body()?.gp_api_message)
            }
        }


    }

    fun onAddQtyClicked() {
        qtySelected.set(qtySelected.get()!! + 1)
        loadOffersData(productDetailstoBeFetched, qtySelected.get())

    }

    fun onMinusQtyClicked() {
        if (qtySelected.get()!! >= 2)
            qtySelected.set(qtySelected.get()!! - 1)
        loadOffersData(productDetailstoBeFetched, qtySelected.get())
    }

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.getParcelable<ProductData>("product") != null) {
            Log.d(
                "ProductName",
                (bundle?.getParcelable<ProductData>("product") as ProductData).product_id.toString()
            )

            productId = (bundle?.getParcelable<ProductData>("product") as ProductData).product_id!!

            productDetailstoBeFetched.product_id = productId

            loadProductDataJob?.takeIf { it.isActive }?.cancel()

            loadProductDataJob = viewModelScope.launch {

                //Start Loader

                progressLoader.set(true)
                val productAPIResponse = productRepository
                    .getProductData(productDetailstoBeFetched)
                //stop loader
                progressLoader.set(false)
                if (productAPIResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                    productData.set(productAPIResponse.body()?.gpApiResponseData!!)
                    productData.let {
                        val productResponseData = productData.get()
                        getNavigator()?.setToolbarTitle(productResponseData?.productBaseName!!)
                        isHeartSelected.set(productResponseData?.isUserFavourite)
                        productResponseData?.productImages?.let {
                            getNavigator()?.setProductImagesViewPagerAdapter(
                                ProductImagesAdapter(
                                    getNavigator()?.getFragmentManagerPager()!!,
                                    productResponseData?.productImages!!
                                )
                            )
                        }

                        mProductDetailsKeyValues =
                            (productResponseData?.productDetails?.keyPoints!!).toMutableList()

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
                                Log.d("productSKUItemSelected", it.productId.toString())
                                selectedSkuListItem.set(it)
                                productDetailstoBeFetched.product_id =
                                    selectedSkuListItem.get()?.productId!!.toInt()

                                setPercentage_mrpVisibility(selectedSkuListItem.get()!!, selectedOfferItem)

                            }

                            val productIdDefault = productResponseData.productIdDefault
                            for (item in mSKUList) {
                                item?.selected = item?.productId!!.equals(productIdDefault)
                                if (item?.selected == true) {
                                    selectedSkuListItem.set(item)
                                    productDetailstoBeFetched.product_id =
                                        selectedSkuListItem.get()?.productId!!.toInt()
                                    setPercentage_mrpVisibility(
                                        selectedSkuListItem.get()!!,
                                        selectedOfferItem
                                    )

                                }
                            }
                            getNavigator()?.refreshSKUAdapter()

                        }
                    }
                    loadOffersData(productDetailstoBeFetched)
                    loadRelatedProductData(productDetailstoBeFetched)

                } else {
                    getNavigator()?.showToast(productAPIResponse.body()?.gpApiMessage)
                }


            }


        }
    }

    private fun setPercentage_mrpVisibility(
        model: ProductSkuListItem,
        offerModel: PromotionListItem? = null
    ) {
        var isOffersLayoutVisible = true
        var priceDiff: Float = 0.0f
        var finalSalePrice: Double = 0.0
        var finaldiscount = "0"
        var isMRPVisibile = false
        var isContactforPriceVisible = false
        if (model.mrpPrice == null || model.salesPrice == null) {

            isOffersLayoutVisible = false
            isContactforPriceVisible = true
            addToCartEnabled.set(false)
        } else {
            isContactforPriceVisible = false
            addToCartEnabled.set(true)

            if (offerModel == null || offerModel?.amount_saved == 0.0) {
                priceDiff = (model.mrpPrice!!.toFloat() - (model.salesPrice)!!.toFloat())
            } else if (offerModel != null && offerModel.amount_saved!! > 0.0) {
                priceDiff =
                    (model.mrpPrice!!.toFloat() - (model.salesPrice)!!.toFloat()) - offerModel.amount_saved.toFloat()
            }

            val numarator = (priceDiff * 100)
            val denominator = model.salesPrice!!.toFloat()
            val percentage = numarator / denominator
            val formatted_percentage = String.format("%.02f", percentage);
            finaldiscount = (formatted_percentage + " % off")
            isMRPVisibile = priceDiff > 0


            offerModel?.let {
                if (offerModel.amount_saved!! > 0) {
                    finalSalePrice = model.salesPrice.toDouble() - offerModel?.amount_saved!!
                }
            }
            if (offerModel == null || offerModel?.amount_saved == 0.0) {
                finalSalePrice = model.salesPrice.toDouble()
            }
// set offer detailsLayout visibility
            isOffersLayoutVisible = !model.mrpPrice.equals(null)

        }

        getNavigator()?.setPercentageOff_mrpVisibility(
            finalSalePrice.toString(),
            finaldiscount,
            isMRPVisibile, isOffersLayoutVisible, isContactforPriceVisible
        )

    }

    private fun loadRelatedProductData(productDetailstoBeFetched: ProductData) {
        loadRelatedProductDataJob.cancelIfActive()
        loadRelatedProductDataJob = checkNetworkThenRun {
            try {
                val relatedProductResponse =
                    productRepository.getRelatedProductsData(productDetailstoBeFetched)
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
                } else {
                    getNavigator()?.showToast(relatedProductResponse.body()?.gpApiMessage)
                }


            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }


        loadReviewData(productDetailstoBeFetched)
    }

    private fun loadReviewData(productDetailstoBeFetched: ProductData) {

        loadProductReviewsDataJob?.takeIf { it.isActive }?.cancel()
        loadProductReviewsDataJob = checkNetworkThenRun {

            try {

                val productReviewAPIResponse = productRepository.getProductReviewsData(
                    Constants.TOP,
                    null,
                    productDetailstoBeFetched
                )

                val productReviewResponse = productReviewAPIResponse.body()
                if (productReviewResponse?.gpApiStatus.equals(
                        Constants.GP_API_STATUS
                    )
                ) {
                    val productReviewResponseData = productReviewResponse?.gpApiResponseData
                    productReviewsData.set(productReviewResponseData)
                    ratingSelected.set(productReviewResponseData?.selfRating?.rating)
                    getNavigator()?.setRatingBarChangeListener()
                    getNavigator()?.setRatingAndReviewsAdapter(
                        RatingAndReviewsAdapter(
                            productReviewsData.get()?.reviewList?.data as ArrayList<ReviewListItem?>,
                            2
                        )
                    )

                } else {
                    getNavigator()?.showToast(productReviewResponse?.gpApiMessage)
                }


            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }


    }

    private fun loadOffersData(productDetailstoBeFetched: ProductData, quantity: Int? = 0) {
        loadProductOffersDataJob.cancelIfActive()
        loadProductOffersDataJob = checkNetworkThenRun {
            if (quantity!! > 0) {
                productDetailstoBeFetched.quantity = quantity
            }
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
                        selectedOfferItem.let {
                            if (mSkuOfferList.contains(selectedOfferItem)) {
                                for (item in mSkuOfferList) {
                                    if (item!!.equals(selectedOfferItem)) {
                                        item.selected = true
                                    }
                                    getNavigator()?.refreshOfferAdapter()
                                }

                            } else {
                                //do nothing as a new list is loaded with selected = false
                            }
                        }
                        getNavigator()?.setProductSKUOfferAdapter(
                            ProductSKUOfferAdapter(mSkuOfferList),
                            {
                                //When RadioButton is clicked
                                selectedOfferItem = it

                                setPercentage_mrpVisibility(selectedSkuListItem.get()!!, selectedOfferItem)

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


    fun openAddEditProductReview(rating: Double) {

        //If Genuine Customer

        if (!productReviewsData.get()?.selfRating?.is_certified_buyer!!) {
            getNavigator()?.openActivityWithBottomToTopAnimation(
                AddEditProductReviewActivity::class.java,
                Bundle().apply {

                    putInt(Constants.Product_Id_Key, productId)
                    putString(Constants.Product_Base_Name, productData.get()?.productBaseName)
                    putDouble(Constants.RATING_SELECTED, rating)
                    putParcelable(
                        Constants.PRODUCT_RATING_DATA_KEY,
                        productReviewsData.get()?.selfRating
                    )
                })
        } else {

            // else if not genuine customer

            getNavigator()?.showGenuineCustomerRatingDialog(GenuineCustomerRatingAlertFragment()) {
                //callback comes here when on add to cart is clicked
                Log.d("Click", "Add to cart Clicked")
                addToCartJob.cancelIfActive()
                addToCartJob = checkNetworkThenRun {
                    progressLoader.set(true)
                    var producttoBeAdded = ProductData()
                    producttoBeAdded.product_id = productId
                    producttoBeAdded.quantity = 1
                    val addTocartResponse =
                        productRepository.addToCart(producttoBeAdded)

                    if (addTocartResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                        progressLoader.set(false)
                        getNavigator()?.showToast(addTocartResponse.body()?.gp_api_message)
                    } else {
                        progressLoader.set(false)
                        getNavigator()?.showToast(addTocartResponse.body()?.gp_api_message)
                    }
                }

            }

        }
    }

    fun onAddToCartClicked() {

        if (getNavigator()?.updateAddToCartButtonText() == getNavigator()?.getMessage(R.string.add_to_cart)) {
            //because it is called multiple times , hence manually setting text as add to cart until the response is successful.
            getNavigator()?.updateAddToCartButtonText(getNavigator()?.getMessage(R.string.add_to_cart)!!)
            addToCartJob.cancelIfActive()
            addToCartJob = checkNetworkThenRun {
                progressLoader.set(true)
                var producttoBeAdded = ProductData()
                producttoBeAdded.product_id = selectedSkuListItem.get()?.productId!!.toInt()
                producttoBeAdded.quantity = qtySelected.get()
                producttoBeAdded.promotion_id = selectedOfferItem.promotion_id
                val addTocartResponse =
                    productRepository.addToCart(producttoBeAdded)

                if (addTocartResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                    progressLoader.set(false)
                    getNavigator()?.showToast(addTocartResponse.body()?.gp_api_message)
                    getNavigator()?.updateAddToCartButtonText(getNavigator()?.getMessage(R.string.gotocart)!!)

                } else {
                    progressLoader.set(false)
                    getNavigator()?.showToast(addTocartResponse.body()?.gp_api_message)
                }
            }
        } else {
            getNavigator()?.openActivity(CartActivity::class.java)
        }


    }

    fun onExpertAdviceClicked() {
        getNavigator()?.showExpertAdviceDialog(ExpertAdviceBottomSheetFragment(), {

            getNavigator()?.dismissExpertBottomSheet()

        }, {
            //Call ExpertAdviceAPI
            Log.d("Click", "yesPleaseClicked")
            expertAdviceJob.cancelIfActive()
            expertAdviceJob = checkNetworkThenRun {
                progressLoader.set(true)
                var producttoBeAdded = ProductData()
                producttoBeAdded.product_id = productId

                val expertAdviceResponse =
                    productRepository.getHelp(Constants.EXPERT_ADVICE, producttoBeAdded)

                getNavigator()?.dismissExpertBottomSheet()
                progressLoader.set(false)

                if (expertAdviceResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {

                    getNavigator()?.showToast(expertAdviceResponse.body()?.gp_api_message)
                } else {
                    getNavigator()?.showToast(expertAdviceResponse.body()?.gp_api_message)
                }
            }


        })

    }

    fun ContactForPriceClicked() {

        contactForPriceJob.cancelIfActive()
        contactForPriceJob = checkNetworkThenRun {
            progressLoader.set(true)
            var producttoBeAdded = ProductData()
            producttoBeAdded.product_id = productId

            val expertAdviceResponse =
                productRepository.getHelp(Constants.CONTACTFORPRICE, producttoBeAdded)

            progressLoader.set(false)

            if (expertAdviceResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {

                getNavigator()?.showToast(expertAdviceResponse.body()?.gp_api_message)
                getNavigator()?.showContactForPriceBottomSheetDialog(ContactForPriceBottomSheetDialog())
            } else {
                getNavigator()?.showToast(expertAdviceResponse.body()?.gp_api_message)
            }
        }


    }

}







