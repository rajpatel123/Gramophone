package agstack.gramophone.ui.cart

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.cart.model.GpApiResponseData
import agstack.gramophone.ui.cart.model.OfferApplied
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import android.os.Bundle

interface CartNavigator : BaseNavigator {
    fun getBundle(): Bundle?

    fun sendGoToCartMoEngageEvent(redirectionFrom: String)

    fun sendPlaceOrderMoEngageEvent(cartData: GpApiResponseData, orderReferenceId: String)

    fun sendRemoveProductFromCartMoEngageEvent(
        productId: Int,
        productBaseName: String,
        sellingPrice: Float,
        mrpPrice: Float,
        sellingPriceAfterDiscount: Float,
        productSku: String,
        productQuantity: Int,
        offerId: String,
        productDiscount: Float,
    )

    fun setCartAdapter(
        cartAdapter: CartAdapter,
        onItemDetailClicked: (productId: String) -> Unit,
        onCartItemDeleteClicked: (productId: String, cartItem: CartItem) -> Unit,
        onOfferClicked: (offerAppliedList: OfferApplied, productName: String, productSku: String) -> Unit,
        onQuantityClicked: (cartItem: CartItem) -> Unit,
    )

    fun openProductDetailsActivity(productData: ProductData)

    fun openCheckoutStatusActivity(bundle: Bundle)

    fun openHomeActivity()
}
