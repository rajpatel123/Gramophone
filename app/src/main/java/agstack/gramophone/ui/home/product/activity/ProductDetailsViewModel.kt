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
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.NonNullObservableField
import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<ProductDetailsNavigator>() {
    val productDetailstoBeFetched = ProductData()
    var mSKUList = ArrayList<ProductSkuListItem?>()

    var mSkuOfferList = ArrayList<PromotionListItem?>()

    var productData = ObservableField<GpApiResponseDataProduct?>()
    var productReviewsData = ObservableField<GpApiResponseData?>()
    var relatedProductData = ObservableField<GpApiResponseDataRelatedProduct?>()
    var productId: Int = 0
    lateinit var mProductDetailsList: MutableList<ProductDetailsItem?>
    private var loadProductDataJob: Job? = null
    private var loadRelatedProductDataJob: Job? = null
    private var loadProductReviewsDataJob: Job? = null
    private var loadProductOffersDataJob: Job? = null
    private var checkPromotionApplicableJob: Job? = null

    private var addToCartJob: Job? = null
    private var expertAdviceJob: Job? = null
    private var updateProductFavoriteJob: Job? = null
    private var contactForPriceJob: Job? = null
    var progressLoader = ObservableField<Boolean>(false)

    //Values selected by User
    var qtySelected = ObservableField<Int>(1)

    var ratingSelected = ObservableField<Double>(0.0)
    var isHeartSelected = NonNullObservableField<Boolean>(false)
    var selectedSkuListItem = ObservableField<ProductSkuListItem>()
    var selectedOfferItem = PromotionListItem()
    var addToCartEnabled = NonNullObservableField<Boolean>(true)
    var isApplicableChanged = ObservableField<Boolean>(false)
    fun onHeartIconClicked() {
        isHeartSelected.set(!isHeartSelected.get())
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
                (bundle.getParcelable<ProductData>("product") as ProductData).product_id.toString()
            )

            productId = (bundle.getParcelable<ProductData>("product") as ProductData).product_id!!

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
                        isHeartSelected.set(productResponseData?.isUserFavourite!!)
                        productResponseData.productImages?.let {
                            getNavigator()?.setProductImagesViewPagerAdapter(
                                ProductImagesAdapter(
                                    getNavigator()?.getFragmentManagerPager()!!,
                                    productResponseData.productImages
                                )
                            )
                        }

                        productResponseData.productDetails?.let {
                            //product Details could be null


                            mProductDetailsList =
                                (productResponseData.productDetails!!).toMutableList()
                            var detailTypeKeyValueList = HashMap<String, ArrayList<KeyPointsItem>>()

                            var keyArrayList = ArrayList<String>()
                            for (value in mProductDetailsList) {
                                keyArrayList.add(value?.productDetailType!!)
                            }

                            if (keyArrayList.size > 1) {

                                val hset: HashSet<String> = HashSet<String>(keyArrayList)
                                keyArrayList = ArrayList<String>(hset)
                            }


                            for (keyValue in keyArrayList) {
                                var keyValueArrayList = ArrayList<KeyPointsItem>()
                                for (value in mProductDetailsList) {
                                    if (keyValue.equals(value?.productDetailType)) {
                                        keyValueArrayList.add(
                                            KeyPointsItem(
                                                value?.productDetailKey,
                                                value?.productDetailValue
                                            )
                                        )
                                    }
                                }
                                detailTypeKeyValueList.put(keyValue, keyValueArrayList)

                            }


                            //set ProductDetails Adapter
                            getNavigator()?.setProductDetailsAdapter(
                                ProductDetailsAdapter(detailTypeKeyValueList)
                            )
                        }

                        //set skuList
                        mSKUList = ArrayList(productData?.get()?.productSkuList)

                        mSKUList.let {
                            getNavigator()?.setProductSKUAdapter(
                                ProductSKUAdapter(
                                    mSKUList
                                ) {

                                }
                            ) {
                                Log.d("productSKUItemSelected", it.productId.toString())
                                selectedSkuListItem.set(it)
                                productDetailstoBeFetched.product_id =
                                    selectedSkuListItem.get()?.productId!!.toInt()
                                //Refresh offerList when product SKU is selected


                                    setPercentage_mrpVisibility(
                                        selectedSkuListItem.get()!!,
                                        null
                                    )
                                    //reset the selectedOfferItem
                                    selectedOfferItem= PromotionListItem()

                                    loadOffersData(productDetailstoBeFetched, qtySelected.get())




                            }

                            val productIdDefault = productResponseData?.productIdDefault
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
                    loadOffersData(productDetailstoBeFetched, qtySelected.get())
                    loadRelatedProductData(productDetailstoBeFetched)

                } else {
                    getNavigator()?.showToast(productAPIResponse.body()?.gpApiMessage)
                }


            }


        }
    }

    private fun setPercentage_mrpVisibility(
        model: ProductSkuListItem,
        offerModel: PromotionListItem? = null,
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

            var amountSaved = 0f
            if (offerModel?.amount_saved.isNotNull()) {
                amountSaved = offerModel?.amount_saved!!.toFloat()
            }

            if (offerModel == null || amountSaved==0f) {
                priceDiff = (model.mrpPrice!!.toFloat() - (model.salesPrice)!!.toFloat())
            } else if (offerModel != null && amountSaved > 0f) {
                priceDiff =
                    (model.mrpPrice!!.toFloat() - (model.salesPrice)!!.toFloat()) -amountSaved
            }

            val numarator = (priceDiff * 100)
            val denominator = model.mrpPrice!!.toFloat()
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
            progressLoader.set(true)
            if (quantity!! > 0) {
                productDetailstoBeFetched.quantity = quantity
            }
            val offersOnProductResponse =
                productRepository.getOffersOnProductData(productDetailstoBeFetched)
            progressLoader.set(false)
            if (offersOnProductResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
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
                            ProductSKUOfferAdapter(
                                mSkuOfferList,
                                selectedSkuListItem.get()!!,
                                {},
                                {}),
                            {
                                //When RadioButton is clicked
                                selectedOfferItem = it
                                if (checkPromotionApplicable(
                                        selectedOfferItem,
                                        selectedSkuListItem.get()!!,
                                        qtySelected.get()!!
                                    )
                                ) {
                                    //Selected Offer is Applicable on selectedSKU

                                    for (item in mSkuOfferList) {
                                        item?.selected =
                                            selectedOfferItem.promotion_id!!.equals(item?.promotion_id)

                                    }
                                    getNavigator()?.refreshOfferAdapter()



                                    setPercentage_mrpVisibility(
                                        selectedSkuListItem.get()!!,
                                        selectedOfferItem
                                    )
                                }

                            },
                            {
                                //when view all is clicked
                                getNavigator()?.openActivity(
                                    OfferDetailActivity::class.java,
                                    Bundle().apply {

                                        var offersDataItem = DataItem()
                                        offersDataItem.endDate = it.valid_till
                                        offersDataItem.productName = it.title
                                        offersDataItem.productsku = it.applicable_on_sku
                                        offersDataItem.image = it.image
                                        offersDataItem.termsConditions = it.t_c
                                        putParcelable(Constants.OFFERSDATA, offersDataItem)

                                    })
                            })
                    }
                }
            }
        }


    }

    private fun checkPromotionApplicable(
        selectedOfferItem: PromotionListItem,
        selectedSKU: ProductSkuListItem,
        quantity: Int
    ): Boolean {

        var isApplicable = false
        checkPromotionApplicableJob.cancelIfActive()
        checkPromotionApplicableJob = checkNetworkThenRun {
            var verifyPromotionsModel = VerifyPromotionRequestModel(
                selectedSKU.productId!!,
                quantity,
                selectedOfferItem.promotion_id?.toString()!!
            )


            val offersOnProductResponse =
                productRepository.checkPromotionOnProduct(verifyPromotionsModel)
            isApplicable =
                offersOnProductResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)


            isApplicableChanged.set(isApplicable)
            /*    isApplicableChanged.addOnPropertyChangedCallback((new OnPropertyChangedCallback() ) -> {
                // Update marker object
            })*/


            if (!isApplicable) {
                getNavigator()?.showToast(offersOnProductResponse.body()?.gpApiMessage)
            }
        }
        return true


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

        if (productReviewsData.get()?.selfRating?.is_certified_buyer!!) {
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


            getNavigator()?.showGenuineCustomerRatingDialog(
                GenuineCustomerRatingAlertFragment.newInstance(
                    addToCartEnabled.get()!!
                ), addToCartEnabled.get()!!
            ) {
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
                        onAddToCartClicked()
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
                getNavigator()?.showContactForPriceBottomSheetDialog(
                    ContactForPriceBottomSheetDialog()
                )
            } else {
                getNavigator()?.showToast(expertAdviceResponse.body()?.gp_api_message)
            }
        }


    }

}







