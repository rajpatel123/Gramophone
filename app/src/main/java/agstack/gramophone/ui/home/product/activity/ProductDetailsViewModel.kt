package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.home.product.ProductDetailsAdapter
import agstack.gramophone.ui.home.product.activity.productreview.AddEditProductReviewActivity
import agstack.gramophone.ui.home.product.fragment.ContactForPriceBottomSheetDialog
import agstack.gramophone.ui.home.product.fragment.ExpertAdviceBottomSheetFragment
import agstack.gramophone.ui.home.product.fragment.GenuineCustomerRatingAlertFragment
import agstack.gramophone.ui.home.product.fragment.RelatedProductFragmentAdapter
import agstack.gramophone.ui.home.subcategory.AvailableProductOffersAdapter
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
import com.amnix.xtension.extensions.isNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.math.roundToInt

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
    var promotionId_applied_fromgetCart: String? = null
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
        calculateDiscountAndPromotion(
            selectedSkuListItem.get()!!,
            null
        )

    }

    fun onMinusQtyClicked() {
        if (qtySelected.get()!! > 1) {
            qtySelected.set(qtySelected.get()!! - 1)
            loadOffersData(productDetailstoBeFetched, qtySelected.get())
            calculateDiscountAndPromotion(
                selectedSkuListItem.get()!!,
                null
            )
        }
        //check if available in getCartAPI , if yes then call addtocart
        /*    var cartItemsList = getCartData()
            if (cartItemsList != null && cartItemsList.size>0) {
                for (cartItem in cartItemsList) {

                    if(cartItem.product_id.equals(selectedSkuListItem.get()?.productId)){
                        //call addtocart API

                        val productData = ProductData()
                        productData.product_id = cartItem.product_id.toInt()
                        productData.quantity =qtySelected.get()
                        updateCart(productData)
                    }
                }


                }*/

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
                                qtySelected.set(1)
                                getNavigator()?.updateAddToCartButtonText(
                                    getNavigator()?.getMessage(
                                        R.string.add_to_cart
                                    )!!
                                )
                                checkIfSelectedSKUPresentInCart_UpdateQty(selectedSkuListItem)
                                productDetailstoBeFetched.product_id =
                                    selectedSkuListItem.get()?.productId!!.toInt()

                                //Refresh offerList when product SKU is selected


                                calculateDiscountAndPromotion(
                                    selectedSkuListItem.get()!!,
                                    null
                                )
                                //reset the selectedOfferItem
                                selectedOfferItem = PromotionListItem()

                                loadOffersData(productDetailstoBeFetched, qtySelected.get())


                            }

                            val productIdDefault = productResponseData?.productIdDefault
                            for (item in mSKUList) {
                                item?.selected = item?.productId!!.equals(productIdDefault)
                                if (item?.selected == true) {
                                    selectedSkuListItem.set(item)



                                    productDetailstoBeFetched.product_id =
                                        selectedSkuListItem.get()?.productId!!.toInt()
                                    calculateDiscountAndPromotion(
                                        selectedSkuListItem.get()!!,
                                        selectedOfferItem
                                    )

                                }
                            }
                            qtySelected.set(1)
                            checkIfSelectedSKUPresentInCart_UpdateQty(selectedSkuListItem)


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

    private fun checkIfSelectedSKUPresentInCart_UpdateQty(selectedSkuListItem: ObservableField<ProductSkuListItem>) {

        var cartItemsList = getCartData()
        if (cartItemsList != null && cartItemsList?.size!! > 0) {
            for (cartItem in cartItemsList!!) {
                if (cartItem.product_id.equals(selectedSkuListItem.get()?.productId)) {
                    qtySelected.set(cartItem.quantity)
                    promotionId_applied_fromgetCart = cartItem.offer_applied.promotion_id
                    //select the Promotion/OfferItem from the main offerlist with  promotion ID = promotionIdCartItem


                    //change add to cart to go toCart
                    getNavigator()?.updateAddToCartButtonText(
                        getNavigator()?.getMessage(
                            R.string.gotocart
                        )!!
                    )
                }

            }


        }

    }

    private fun getCartData(): List<CartItem>? {
        var cartItemsList: List<CartItem>? = null
        viewModelScope.launch {
            try {

                if (getNavigator()?.isNetworkAvailable() == true) {
                    progressLoader.set(true)
                    val response = productRepository.getCartData()
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data?.cart_items != null && response.body()?.gp_api_response_data?.cart_items?.size!! > 0
                    ) {

                        cartItemsList = response.body()?.gp_api_response_data?.cart_items

                    } else {

                    }

                    progressLoader.set(false)
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progressLoader.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
        return cartItemsList
    }

    private fun calculateDiscountAndPromotion(
        model: ProductSkuListItem,
        offerModel: PromotionListItem? = null,
    ) {
        if (model.isNull()) return
        var modelMrpPrice = 0f
        var modelSalesPrice = 0f
        var finalSalePrice = 0f
        var finalDiscount = "0"
        var isMRPVisible = false
        var isDiscountPercentVisible = false
        var isContactForPriceVisible = false
        var proceedForCalculation = true
        var isOffersLayoutVisible = true
        addToCartEnabled.set(true)

        modelMrpPrice = if (model.mrpPrice.isNull()) 0f else model.mrpPrice!!.toFloat()
        modelSalesPrice = if (model.salesPrice.isNullOrEmpty()) 0f else model.salesPrice.toFloat()

        if (modelMrpPrice == 0f && modelSalesPrice > 0f) {
            modelMrpPrice = modelSalesPrice
        } else if (modelSalesPrice == 0f && modelMrpPrice > 0f) {
            modelSalesPrice = modelMrpPrice
        } else if (modelMrpPrice > 0f && modelSalesPrice > 0f) {
            // do no assignment in this case
        } else {
            modelMrpPrice = 0f
            modelSalesPrice = 0f
            isContactForPriceVisible = true
            proceedForCalculation = false
            isOffersLayoutVisible = false
            addToCartEnabled.set(false)
        }
        modelMrpPrice *= qtySelected.get()!!
        modelSalesPrice *= qtySelected.get()!!
        var discountPercent = 0f
        if (proceedForCalculation) {
            isMRPVisible = modelMrpPrice > modelSalesPrice
            discountPercent =
                ((modelMrpPrice - modelSalesPrice) / modelMrpPrice) * 100
            finalDiscount = (String.format("%.0f", discountPercent) + "% off")
            isDiscountPercentVisible = discountPercent > 0

            finalSalePrice = modelSalesPrice
            if (offerModel.isNotNull())
                offerModel?.let {
                    if (offerModel.benefit.isNotNull() && offerModel.benefit?.amount_saved.isNotNull() && offerModel.benefit?.amount_saved!! > 0) {
                        finalSalePrice = modelSalesPrice - offerModel.benefit.amount_saved.toFloat()

                        discountPercent =
                            ((modelMrpPrice - finalSalePrice) / modelMrpPrice) * 100
                        finalDiscount = (String.format("%.0f", discountPercent) + "% off")
                        isDiscountPercentVisible = discountPercent > 0
                    }
                }
        }

        getNavigator()?.updateUIAfterCalculation(
            model.product_app_name!!,
            finalSalePrice,
            modelMrpPrice,
            finalDiscount,
            isDiscountPercentVisible,
            isMRPVisible, isOffersLayoutVisible, isContactForPriceVisible
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

                                promotionId_applied_fromgetCart?.let {
                                    for (offerItem in mSkuOfferList) {
                                        if (offerItem?.promotion_id!!.equals(
                                                promotionId_applied_fromgetCart
                                            )
                                        ) {
                                            offerItem.selected = true
                                            selectedOfferItem = offerItem
                                            getNavigator()?.refreshOfferAdapter()
                                        }

                                    }
                                }
                                //do nothing as a new list is loaded with selected = false
                            }
                        }
                        var selectedSkuPrice = 0f
                        if (selectedSkuListItem.get().isNotNull()) {
                            val item: ProductSkuListItem = selectedSkuListItem.get()!!
                            val mrpPrice =
                                if (item.mrpPrice.isNull()) 0f else item.mrpPrice!!.toFloat()
                            val salesPrice =
                                if (item.salesPrice.isNullOrEmpty()) 0f else item.salesPrice.toFloat()

                            selectedSkuPrice = if (salesPrice == 0f) {
                                mrpPrice * quantity
                            } else {
                                salesPrice * quantity
                            }
                        }
                        getNavigator()?.setProductSKUOfferAdapter(AvailableProductOffersAdapter(
                            mSkuOfferList,
                            selectedSkuPrice,
                            {
                                selectedOfferItem = it
                                checkOfferApplicability(VerifyPromotionRequestModel(
                                    selectedSkuListItem.get()?.productId!!,
                                    quantity,
                                    it.promotion_id.toString()))
                            },
                            {
                                //when view all is clicked
                                getNavigator()?.openActivity(
                                    OfferDetailActivity::class.java,
                                    Bundle().apply {

                                        val offersDataItem = DataItem()
                                        offersDataItem.endDate = it.valid_till
                                        offersDataItem.productName = it.title
                                        offersDataItem.productsku = it.applicable_on_sku
                                        offersDataItem.image = it.image
                                        offersDataItem.termsConditions = it.t_c
                                        putParcelable(Constants.OFFERSDATA, offersDataItem)

                                    })
                            }))
                    }
                }
            }
        }
    }

    private fun checkOfferApplicability(
        verifyPromotionsModel: VerifyPromotionRequestModel,
    ) {
        checkPromotionApplicableJob.cancelIfActive()
        checkPromotionApplicableJob = checkNetworkThenRun {
            try {
                val response =
                    productRepository.checkPromotionOnProduct(verifyPromotionsModel)

                if (response.body()?.gpApiStatus.equals(Constants.GP_API_STATUS) &&
                    response.body()?.gpApiResponseData?.promotionApplicable == true
                ) {
                    for (item in mSkuOfferList) {
                        item?.selected =
                            selectedOfferItem.promotion_id!! == item?.promotion_id

                    }
                    getNavigator()?.refreshOfferAdapter()
                    calculateDiscountAndPromotion(
                        selectedSkuListItem.get()!!,
                        selectedOfferItem
                    )
                } else {
                    for (item in mSkuOfferList) {
                        item?.selected = false
                    }
                    getNavigator()?.refreshOfferAdapter()
                    getNavigator()?.showToast(if (response.body()?.gpApiMessage.isNull()) "" else response.body()?.gpApiMessage!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                for (item in mSkuOfferList) {
                    item?.selected = false
                }
                getNavigator()?.refreshOfferAdapter()
            }
        }
    }

    private fun checkPromotionApplicable(
        selectedOfferItem: PromotionListItem,
        selectedSKU: ProductSkuListItem,
        quantity: Int,
    ) {

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
                (offersOnProductResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS) && offersOnProductResponse.body()?.gpApiResponseData?.promotionApplicable!!)

            if (isApplicable) {
                for (item in mSkuOfferList) {
                    item?.selected =
                        selectedOfferItem.promotion_id!!.equals(item?.promotion_id)

                }
                getNavigator()?.refreshOfferAdapter()

                calculateDiscountAndPromotion(
                    selectedSkuListItem.get()!!,
                    selectedOfferItem
                )
            } else {
                for (item in mSkuOfferList) {
                    item?.selected = false
                }
                getNavigator()?.refreshOfferAdapter()
                getNavigator()?.showToast(offersOnProductResponse.body()?.gpApiMessage)
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


    private fun updateCart(productData: ProductData) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progressLoader.set(true)
                    val response = productRepository.updateCartItem(productData)
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                        getCartData()
                    }
                    progressLoader.set(false)
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progressLoader.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }


}







