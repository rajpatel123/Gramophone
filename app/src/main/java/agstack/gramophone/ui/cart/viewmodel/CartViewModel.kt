package agstack.gramophone.ui.cart.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.cart.CartNavigator
import agstack.gramophone.ui.cart.repository.CartRepository
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<CartNavigator>() {



}
