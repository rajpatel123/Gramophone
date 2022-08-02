package agstack.gramophone.ui.cart

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.cart.model.OfferApplied
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import android.os.Bundle

interface CartNavigator : BaseNavigator {

    fun setCartAdapter(
        cartAdapter: CartAdapter,
        onItemDetailClicked: (productId: String) -> Unit,
        onCartItemDeleteClicked: (productId: String) -> Unit,
        onOfferClicked: (offerAppliedList: List<OfferApplied>) -> Unit,
        onQuantityClicked: (cartItem: CartItem) -> Unit,
    )

    fun openProductDetailsActivity(productData: ProductData)

    fun openAppliedOfferDetailActivity(offerAppliedList: List<OfferApplied>)

    fun openCheckoutStatusActivity(bundle: Bundle)

    fun openHomeActivity()
}
