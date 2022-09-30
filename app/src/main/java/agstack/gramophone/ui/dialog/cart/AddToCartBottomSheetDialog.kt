package agstack.gramophone.ui.dialog.cart

import agstack.gramophone.R
import agstack.gramophone.databinding.BottomSheetAddToCartBinding
import agstack.gramophone.ui.home.product.activity.ProductSKUAdapter
import agstack.gramophone.ui.home.product.activity.ProductSKUOfferAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_add_to_cart.*
import java.lang.Exception
import kotlin.math.roundToInt


class AddToCartBottomSheetDialog(
    private val selectedPromotion: ((PromotionListItem) -> Unit)?,
    private val onAddToCart: ((ProductData) -> Unit)?,
) :
    BottomSheetDialogFragment() {
    var binding: BottomSheetAddToCartBinding? = null
    lateinit var mSKUList: ArrayList<ProductSkuListItem?>
    lateinit var mSkuOfferList: ArrayList<PromotionListItem?>
    lateinit var productData: ProductData
    var selectedSkuListItem = ObservableField<ProductSkuListItem>()
    val productDetailstoBeFetched = ProductData()
    var selectedOfferItem = PromotionListItem()
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
        }
        setPercentage_mrpVisibility(selectedSkuListItem.get()!!, selectedOfferItem)

        binding?.rvProductSku?.adapter = ProductSKUAdapter(mSKUList) {
            Log.d("productSKUItemSelected", it.productId.toString())
            selectedSkuListItem.set(it)
            productDetailstoBeFetched.product_id =
                selectedSkuListItem.get()?.productId!!.toInt()

            setPercentage_mrpVisibility(selectedSkuListItem.get()!!, selectedOfferItem)
        }
        binding?.rvAvailableoffers?.adapter = ProductSKUOfferAdapter(mSkuOfferList, {
            selectedOfferItem = it
            setPercentage_mrpVisibility(selectedSkuListItem.get()!!, selectedOfferItem)
        }, {
            selectedPromotion?.invoke(it)
        })
        binding?.relativeAddToCart?.setOnClickListener {
            productData.quantity = quantity
            onAddToCart?.invoke(productData)
            dismiss()
        }
        binding?.tvPlus?.setOnClickListener {
            quantity += 1
            binding?.tvQTY?.text = quantity.toString()
        }
        binding?.tvMinus?.setOnClickListener {
            if (quantity > 1)
                quantity -= 1
            binding?.tvQTY?.text = quantity.toString()
        }
    }

    private fun setPercentage_mrpVisibility(
        model: ProductSkuListItem,
        offerModel: PromotionListItem? = null,
    ) {
        var isOffersLayoutVisible: Boolean
        var finalSalePrice = 0.0
        var finalDiscount = "0"
        var isMRPVisible = false
        var isDiscountPercentVisible = false
        var isContactForPriceVisible: Boolean
        var modelMrpPrice = 0f
        var modelSalesPrice = 0f
        if (model.mrpPrice.isNullOrEmpty() && model.salesPrice.isNullOrEmpty()) {
            modelMrpPrice = 0f
            modelSalesPrice = 0f
            isOffersLayoutVisible = false
            isContactForPriceVisible = true
            binding?.relativeAddToCart?.isEnabled = false
        } else {
            isOffersLayoutVisible = true
            isContactForPriceVisible = false
            binding?.relativeAddToCart?.isEnabled = true
            var proceedForCalculation: Boolean

            if (model.salesPrice.isNullOrEmpty() && !model.mrpPrice.isNullOrEmpty()) {
                    modelSalesPrice = model.mrpPrice.toFloat()
                    modelMrpPrice = model.mrpPrice.toFloat()
                proceedForCalculation = true
            } else if (model.mrpPrice.isNullOrEmpty() && !model.salesPrice.isNullOrEmpty()){
                modelSalesPrice = model.salesPrice.toFloat()
                modelMrpPrice = model.salesPrice.toFloat()
                proceedForCalculation = true
            } else {
                try {
                    modelSalesPrice = model.salesPrice!!.toFloat()
                    modelMrpPrice = model.mrpPrice!!.toFloat()
                    proceedForCalculation = true
                } catch (e: Exception) {
                    proceedForCalculation = false
                    isOffersLayoutVisible = false
                    isContactForPriceVisible = true
                    binding?.relativeAddToCart?.isEnabled = false
                }
            }

            if (proceedForCalculation) {
                isMRPVisible = modelMrpPrice > modelSalesPrice
                val discountPercent =
                    ((modelMrpPrice - modelSalesPrice) / modelMrpPrice) * 100
                finalDiscount = (String.format("%.0f", discountPercent) + " % off")
                isDiscountPercentVisible =
                    !(discountPercent.toString().contains("0.0") || discountPercent.toString()
                        .contains(".00"))

                offerModel?.let {
                    if (offerModel.amount_saved!! > 0) {
                        finalSalePrice = modelSalesPrice.toDouble() - offerModel.amount_saved
                    }
                }
                if (offerModel == null || offerModel.amount_saved == 0.0) {
                    finalSalePrice = modelSalesPrice.toDouble()
                }
            }
        }
        binding?.tvProductMRP?.text = getString(R.string.rupee) + modelMrpPrice.toString()
        binding?.tvDiscountPercent?.text = finalDiscount
        setPercentageOffMrpVisibility(
            finalSalePrice.toString(),
            isDiscountPercentVisible,
            isMRPVisible, isOffersLayoutVisible, isContactForPriceVisible
        )
    }

    private fun setPercentageOffMrpVisibility(
        salesPrice: String,
        isDiscountPercentVisible: Boolean,
        isMRPVisible: Boolean,
        isOffersLayoutVisible: Boolean,
        isContactForPriceVisible: Boolean,
    ) {
        binding?.tvProductSP?.text = getString(R.string.rupee) + salesPrice
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
        } else {
            binding?.tvContactForPrice?.visibility = View.GONE
            binding?.llPriceDiscount?.visibility = View.VISIBLE
            binding?.llQty?.visibility = View.VISIBLE
            binding?.tvInclusive?.visibility = View.VISIBLE
        }

        if (!isOffersLayoutVisible) {
            //v2_separator
            binding?.v2Separator?.visibility = View.GONE
            binding?.rlAvailableOffers?.visibility = View.GONE
        } else {
            binding?.v2Separator?.visibility = View.VISIBLE
            binding?.rlAvailableOffers?.visibility = View.VISIBLE
        }
    }
}