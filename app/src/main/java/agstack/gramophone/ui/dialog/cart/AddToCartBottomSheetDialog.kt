package agstack.gramophone.ui.dialog.cart

import agstack.gramophone.R
import agstack.gramophone.databinding.BottomSheetAddToCartBinding
import agstack.gramophone.ui.home.product.activity.ProductSKUAdapter
import agstack.gramophone.ui.home.product.activity.ProductSKUOfferAdapter
import agstack.gramophone.ui.home.shopbydetail.ShopByDetailActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddToCartBottomSheetDialog(val selectedPromotion: ((PromotionListItem) -> Unit)?,
                                 val onAddToCart: ((ProductData) -> Unit)?) :
    BottomSheetDialogFragment() {
    lateinit var mSKUList: ArrayList<ProductSkuListItem?>
    lateinit var mSkuOfferList: ArrayList<PromotionListItem?>
    lateinit var productData: ProductData
    var binding: BottomSheetAddToCartBinding? = null
    var selectedSkuListItem = ObservableField<ProductSkuListItem>()
    val productDetailstoBeFetched = ProductData()
    var addToCartEnabled = ObservableField<Boolean>(true)
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
        selectedSkuListItem.set(mSKUList[0])
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
            val formatted_percentage = String.format("%.0f", percentage);
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
        binding?.tvProductMRP?.text = model.mrpPrice.toString()
        setPercentageOffMrpVisibility(
            finalSalePrice.toString(),
            finaldiscount,
            isMRPVisibile, isOffersLayoutVisible, isContactforPriceVisible
        )
    }

    fun setPercentageOffMrpVisibility(
        salesPrice: String,
        discountPercentage: String,
        isMRPVisible: Boolean,
        isOffersLayoutVisible: Boolean,
        isContactForPriceVisible: Boolean,
    ) {
        binding?.tvProductSP?.text = getString(R.string.rupee) + salesPrice
        binding?.tvPercentageOffOnSelectedSKU?.text = discountPercentage
        if (isMRPVisible)
            binding?.tvProductMRP?.visibility = View.VISIBLE
        else
            binding?.tvProductMRP?.visibility = View.GONE


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