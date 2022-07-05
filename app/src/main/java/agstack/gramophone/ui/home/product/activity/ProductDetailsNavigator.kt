package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.product.model.ProductSkuOfferModel
import agstack.gramophone.ui.home.product.model.ProductWeightPriceModel
import android.os.Bundle

interface ProductDetailsNavigator: BaseNavigator {
    fun getBundle(): Bundle?
    fun setToolbarTitle(title:String?)
    fun setProductSKUAdapter(productSKUAdapter: ProductSKUAdapter, onSKUItemClicked: (ProductWeightPriceModel) -> Unit)
    fun setProductSKUOfferAdapter(productSKUOfferAdapter: ProductSKUOfferAdapter, onOfferItemClicked: (ProductSkuOfferModel) -> Unit)
}