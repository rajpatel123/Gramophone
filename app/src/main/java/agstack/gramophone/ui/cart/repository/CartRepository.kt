package agstack.gramophone.ui.cart.repository

import agstack.gramophone.di.GramAppService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val gramAppService: GramAppService
) {}