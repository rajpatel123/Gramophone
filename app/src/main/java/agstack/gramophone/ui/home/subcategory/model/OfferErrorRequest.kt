package agstack.gramophone.ui.home.subcategory.model

data class OfferErrorRequest(
    val message: String,
    val product_id: String,
    val promotion_id: Int,
    val status: Boolean
)