package agstack.gramophone.ui.dialog.cart

import agstack.gramophone.R
import agstack.gramophone.data.repository.promotions.PromotionsRepository
import agstack.gramophone.databinding.BottomSheetAddToCartBinding
import agstack.gramophone.ui.home.product.activity.ProductSKUAdapter
import agstack.gramophone.ui.home.subcategory.AvailableProductOffersAdapter
import agstack.gramophone.ui.home.subcategory.model.*
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.utils.Utility
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewDebug
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.async
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNull
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class AddToCartBottomSheetDialog(
    private val selectedPromotion: ((Offer) -> Unit)?,
    private val applyOfferOnProduct: ((OfferForProduct) -> Unit)?,
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
    val productDetailstoBeFetched = ProductData()
    var selectedOfferItem: Offer? = null
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

    var selectedSkuPrice = 0f
    private fun setupUi() {
        if (!mSKUList.isNullOrEmpty()) {
            selectedSkuListItem.set(mSKUList[0])
            mSKUList[0]?.selected = true
            setSelectedSkuPrice(selectedSkuListItem.get()!!)
        }
        setPercentage_mrpVisibility(selectedSkuListItem.get()!!, selectedOfferItem)

        binding?.rvProductSku?.adapter = ProductSKUAdapter(mSKUList) {
            Log.d("productSKUItemSelected", it.productId.toString())
            selectedSkuListItem.set(it)
            setSelectedSkuPrice(it)
            productDetailstoBeFetched.product_id =
                selectedSkuListItem.get()?.productId!!.toInt()

            setPercentage_mrpVisibility(selectedSkuListItem.get()!!, selectedOfferItem)
        }
        if (mSkuOfferList.isNullOrEmpty()) {
            binding?.v2Separator?.visibility = View.GONE
            binding?.rlAvailableOffers?.visibility = View.GONE
        } else {
            binding?.v2Separator?.visibility = View.VISIBLE
            binding?.rlAvailableOffers?.visibility = View.VISIBLE

            binding?.rvAvailableoffers?.adapter =
                AvailableProductOffersAdapter(mSkuOfferList, selectedSkuPrice, {
                    selectedOfferItem = it
                    setPercentage_mrpVisibility(selectedSkuListItem.get()!!, selectedOfferItem)
                    /*applyOfferOnProduct(OfferForProduct(selectedSkuListItem.get()?.productId!!,
                        it.promotion_id,
                        quantity,
                        selectedSkuPrice.toString()))*/
                    applyOfferOnProduct?.invoke(OfferForProduct(selectedSkuListItem.get()?.productId!!,
                        it.promotion_id,
                        quantity,
                        selectedSkuPrice.toString()))
                }, {
                    selectedPromotion?.invoke(it)
                })
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
            setPercentage_mrpVisibility(selectedSkuListItem.get()!!, selectedOfferItem)
        }
        binding?.ivSubtract?.setOnClickListener {
            if (quantity > 1)
                quantity -= 1
            binding?.tvQTY?.text = quantity.toString()
            setPercentage_mrpVisibility(selectedSkuListItem.get()!!, selectedOfferItem)
        }
    }

    private fun setSelectedSkuPrice(selectedSkuListItem: ProductSkuListItem) {
        if (selectedSkuListItem.mrpPrice.isNullOrEmpty() && !selectedSkuListItem.salesPrice.isNullOrEmpty()) {
            selectedSkuPrice = mSKUList[0]?.salesPrice!!.toFloat()
        } else if (selectedSkuListItem.salesPrice.isNullOrEmpty() && !selectedSkuListItem.mrpPrice.isNullOrEmpty()) {
            selectedSkuPrice = mSKUList[0]?.mrpPrice!!.toFloat()
        } else {
            selectedSkuPrice = 0f
        }
    }

    private fun setPercentage_mrpVisibility(
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
        var proceedForCalculation = false

        if (model.mrpPrice.isNullOrEmpty() && model.salesPrice.isNullOrEmpty()) {
            modelMrpPrice = 0f
            modelSalesPrice = 0f
            isContactForPriceVisible = true
        } else if (model.salesPrice.isNullOrEmpty() && !model.mrpPrice.isNullOrEmpty()) {
            modelSalesPrice = model.mrpPrice.toFloat()
            modelMrpPrice = model.mrpPrice.toFloat()
            proceedForCalculation = true
        } else if (model.mrpPrice.isNullOrEmpty() && !model.salesPrice.isNullOrEmpty()) {
            modelSalesPrice = model.salesPrice.toFloat()
            modelMrpPrice = model.salesPrice.toFloat()
            proceedForCalculation = true
        } else {
            try {
                modelSalesPrice = model.salesPrice!!.toFloat()
                modelMrpPrice = model.mrpPrice!!.toFloat()
                proceedForCalculation = true
            } catch (e: Exception) {
                isContactForPriceVisible = true
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

            finalSalePrice = modelSalesPrice
            offerModel?.let {
                if (offerModel.amount_saved.isNotNull() && offerModel.amount_saved > 0) {
                    finalSalePrice = modelSalesPrice - offerModel.amount_saved
                }
            }
        }

        binding?.tvProductMRP?.text = getString(R.string.rupee) + modelMrpPrice.toString()
        binding?.tvDiscountPercent?.text = finalDiscount
        setPercentageOffMrpVisibility(
            finalSalePrice,
            isDiscountPercentVisible,
            isMRPVisible, isContactForPriceVisible
        )
    }

    private fun setPercentageOffMrpVisibility(
        finalSalePrice: Float,
        isDiscountPercentVisible: Boolean,
        isMRPVisible: Boolean,
        isContactForPriceVisible: Boolean,
    ) {
        setPrice(quantity, finalSalePrice)
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

    private fun setPrice(quantity: Int, perUnitPrice: Float) {
        binding?.tvProductSP?.text =
            getString(R.string.rupee) + (quantity * perUnitPrice).toString()
    }

    fun updateDialog(
        isShowError: Boolean,
        errorMsg: String,
    ) {
        try {
            if (isShowError) {
                val offerErrResponse = Gson().fromJson(errorMsg, OfferErrorResponse::class.java)
                Toast.makeText(requireContext(), offerErrResponse.message, Toast.LENGTH_LONG)
                    .show()
            } else {

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
