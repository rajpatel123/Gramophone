package agstack.gramophone.ui.cart

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.home.product.activity.ProductSKUOfferAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem

interface CartNavigator : BaseNavigator {

    fun setCartAdapter(
        cartAdapter: CartAdapter,
        onCartItemClicked: (CartItem) -> Unit,
        onCartItemDeleteClicked: (String) -> Unit,
        onOfferClicked: (String) -> Unit
    )

    fun openProductDetailsActivity()

    fun deleteCartItem(productId: String)

    fun openAppliedOfferDetailActivity()
}
