package agstack.gramophone.ui.favourite

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.favourite.model.Data

interface FavouriteProductNavigator: BaseNavigator {
    fun updateProductList(favouriteProductAdapter: FavouriteProductAdapter, onProductClicked: (Data) -> Unit)
}