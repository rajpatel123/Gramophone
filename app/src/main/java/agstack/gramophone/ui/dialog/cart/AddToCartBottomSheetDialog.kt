package agstack.gramophone.ui.dialog.cart

import agstack.gramophone.R
import agstack.gramophone.data.repository.promotions.PromotionsRepository
import agstack.gramophone.databinding.BottomSheetAddToCartBinding
import agstack.gramophone.ui.home.product.activity.ProductSKUAdapter
import agstack.gramophone.ui.home.subcategory.AvailableProductOffersAdapter
import agstack.gramophone.ui.home.subcategory.model.Offer
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.VerifyPromotionRequestModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNull
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject
import kotlin.math.roundToInt


class AddToCartBottomSheetDialog(
    private val viewOfferDetail: ((Offer) -> Unit)?,
    private val applyOfferOnProduct: ((VerifyPromotionRequestModel) -> Unit)?,
    private val onAddToCart: ((ProductData) -> Unit)?,
) :
    BottomSheetDialogFragment() {
    @Inject
    lateinit var promotionsRepository: PromotionsRepository
    var binding: BottomSheetAddToCartBinding? = null
    lateinit var mSKUList: ArrayList<ProductSkuListItem?>
    lateinit var mSkuOfferList: ArrayList<Offer>
    lateinit var productData: ProductData
    var selectedSkuListItem = ObservableField<ProductSkuListItem>()
    var availableProductOffersAdapter: AvailableProductOffersAdapter? = null
    var selectedOfferItem: Offer? = null
    var selectedSkuPrice = 0f
    var price = 0f
    var quantity = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BottomSheetAddToCartBinding.inflate(inflater)

        setupUi()
        return binding!!.root
    }

    private fun setupUi() {
        if (!mSKUList.isNullOrEmpty()) {
            selectedSkuListItem.set(mSKUList[0])
            mSKUList[0]?.selected = true
            productData.product_id = mSKUList[0]?.productId?.toInt()
            setSelectedSkuPrice(if (mSKUList[0]?.mrpPrice.isNull()) 0f else mSKUList[0]?.mrpPrice!!.toFloat(),
                if (mSKUList[0]?.salesPrice.isNullOrEmpty()) 0f else mSKUList[0]?.salesPrice!!.toFloat())
        }
        calculateDiscountAndPromotion(selectedSkuListItem.get()!!, selectedOfferItem)

        binding?.rvProductSku?.adapter = ProductSKUAdapter(mSKUList) {
            Log.d("productSKUItemSelected", it.productId.toString())
            selectedSkuListItem.set(it)
            productData.product_id = it.productId?.toInt()
            setSelectedSkuPrice(if (it.mrpPrice.isNull()) 0f else it.mrpPrice!!.toFloat(),
                if (it.salesPrice.isNullOrEmpty()) 0f else it.salesPrice.toFloat())
            calculateDiscountAndPromotion(selectedSkuListItem.get()!!, selectedOfferItem)
            if (availableProductOffersAdapter.isNotNull())
                availableProductOffersAdapter?.updateSelectedSkuPrice(selectedSkuPrice)
        }
        if (mSkuOfferList.isNullOrEmpty()) {
            binding?.v2Separator?.visibility = View.GONE
            binding?.rlAvailableOffers?.visibility = View.GONE
        } else {
            binding?.v2Separator?.visibility = View.VISIBLE
            binding?.rlAvailableOffers?.visibility = View.VISIBLE

            availableProductOffersAdapter =
                AvailableProductOffersAdapter(mSkuOfferList, selectedSkuPrice, {
                    selectedOfferItem = it
                    calculateDiscountAndPromotion(selectedSkuListItem.get()!!, selectedOfferItem)
                    applyOfferOnProduct?.invoke(VerifyPromotionRequestModel(selectedSkuListItem.get()?.productId!!,
                        quantity,
                        it.promotion_id.toString()))
                }, {
                    viewOfferDetail?.invoke(it)
                })

            binding?.rvAvailableoffers?.adapter = availableProductOffersAdapter

        }

        binding?.tvContactForPrice?.setOnClickListener {
            Toast.makeText(requireContext(), "Your request has been submitted", Toast.LENGTH_SHORT)
                .show()
            dismiss()
        }

        binding?.relativeAddToCart?.setOnClickListener {
            productData.quantity = quantity
            onAddToCart?.invoke(productData)
            dismiss()
        }
        binding?.ivAdd?.setOnClickListener {
            quantity += 1
            binding?.tvQTY?.text = quantity.toString()
            calculateDiscountAndPromotion(selectedSkuListItem.get()!!, selectedOfferItem)
        }
        binding?.ivSubtract?.setOnClickListener {
            if (quantity > 1)
                quantity -= 1
            binding?.tvQTY?.text = quantity.toString()
            calculateDiscountAndPromotion(selectedSkuListItem.get()!!, selectedOfferItem)
        }
    }

    private fun setSelectedSkuPrice(mrpPrice: Float, salesPrice: Float) {
        selectedSkuPrice = if (salesPrice == 0f) {
            mrpPrice * quantity
        } else {
            salesPrice * quantity
        }
    }

    private fun calculateDiscountAndPromotion(
        model: ProductSkuListItem,
        offerModel: Offer? = null,
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
        }
        modelMrpPrice *= quantity
        modelSalesPrice *= quantity
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
                    if (offerModel.benefit.isNotNull() && offerModel.benefit.amount > 0) {
                        finalSalePrice = modelSalesPrice - offerModel.benefit.amount

                        discountPercent =
                            ((modelMrpPrice - finalSalePrice) / modelMrpPrice) * 100
                        finalDiscount = (String.format("%.0f", discountPercent) + "% off")
                        isDiscountPercentVisible = discountPercent > 0
                    }
                }
        }
        if (modelMrpPrice.toString().endsWith(".0") || modelMrpPrice.toString()
                .contains(".00")
        ) {
            binding?.tvProductMRP?.text =
                getString(R.string.rupee) + (modelMrpPrice.roundToInt()).toString()
        } else {
            binding?.tvProductMRP?.text =
                getString(R.string.rupee) + (modelMrpPrice).toString()
        }

        binding?.tvDiscountPercent?.text = finalDiscount
        showHideViewVisibilityAfterCalculation(
            finalSalePrice,
            isDiscountPercentVisible,
            isMRPVisible, isContactForPriceVisible
        )
    }

    private fun showHideViewVisibilityAfterCalculation(
        finalSalePrice: Float,
        isDiscountPercentVisible: Boolean,
        isMRPVisible: Boolean,
        isContactForPriceVisible: Boolean,

        ) {
        setSalesPrice(finalSalePrice)
        if (isMRPVisible)
            binding?.tvProductMRP?.visibility = View.VISIBLE
        else
            binding?.tvProductMRP?.visibility = View.GONE

        if (isDiscountPercentVisible)
            binding?.tvDiscountPercent?.visibility = View.VISIBLE
        else
            binding?.tvDiscountPercent?.visibility = View.GONE

        if (isContactForPriceVisible) {
            binding?.tvContactForPrice?.visibility = View.VISIBLE
            binding?.llPriceDiscount?.visibility = View.GONE
            binding?.llQty?.visibility = View.GONE
            binding?.tvInclusive?.visibility = View.GONE
            binding?.relativeAddToCart?.isEnabled = false
            binding?.relativeAddToCart?.setBackgroundColor(ContextCompat.getColor(requireContext(),
                R.color.grey_border))
        } else {
            binding?.tvContactForPrice?.visibility = View.GONE
            binding?.llPriceDiscount?.visibility = View.VISIBLE
            binding?.llQty?.visibility = View.VISIBLE
            binding?.tvInclusive?.visibility = View.VISIBLE
            binding?.relativeAddToCart?.isEnabled = true
            binding?.relativeAddToCart?.setBackgroundColor(ContextCompat.getColor(requireContext(),
                R.color.orange))
        }
    }

    private fun setSalesPrice(priceAfterDiscount: Float) {
        if (priceAfterDiscount.toString().endsWith(".0") || priceAfterDiscount.toString()
                .contains(".00")
        ) {
            binding?.tvProductSP?.text =
                getString(R.string.rupee) + priceAfterDiscount.roundToInt().toString()
        } else {
            binding?.tvProductSP?.text =
                getString(R.string.rupee) + priceAfterDiscount.toString()
        }

    }

    fun updateDialog(
        isOfferApplicable: Boolean,
        message: String,
    ) {
        try {
            if (isOfferApplicable) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
                    .show()
            } else {
                if (!mSkuOfferList.isNullOrEmpty()) {
                    for (item in mSkuOfferList) {
                        item.selected = false
                    }
                    if (availableProductOffersAdapter.isNotNull())
                        availableProductOffersAdapter?.notifyDataSetChanged()
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
                    .show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
