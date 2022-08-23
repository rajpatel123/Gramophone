package agstack.gramophone.ui.dialog.cart

import agstack.gramophone.databinding.BottomSheetAddToCartBinding
import agstack.gramophone.ui.home.product.activity.ProductSKUAdapter
import agstack.gramophone.ui.home.product.activity.ProductSKUOfferAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddToCartBottomSheetDialog : BottomSheetDialogFragment() {
    lateinit var mSKUList: ArrayList<ProductSkuListItem?>
    lateinit var mSkuOfferList: ArrayList<PromotionListItem?>
    var binding: BottomSheetAddToCartBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAddToCartBinding.inflate(inflater)

        setupUi()
        return binding!!.root
    }

    private fun setupUi() {
        /*
         getNavigator()?.setProductSKUAdapter(
                                ProductSKUAdapter(
                                    mSKUList
                                )
                            ) {
                                Log.d("productSKUItemSelected", it.productId.toString())
                                selectedSkuListItem = it
                                productDetailstoBeFetched.product_id =
                                    selectedSkuListItem.productId!!.toInt()

                                setPercentage_mrpVisibility(selectedSkuListItem, selectedOfferItem)

                            }
        * */

        binding?.rvProductSku?.adapter = ProductSKUAdapter(mSKUList)
        binding?.rvAvailableoffers?.adapter = ProductSKUOfferAdapter(mSkuOfferList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}