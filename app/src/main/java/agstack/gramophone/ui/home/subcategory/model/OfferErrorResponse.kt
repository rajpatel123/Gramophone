package agstack.gramophone.ui.home.subcategory.model

data class OfferErrorResponse(
    val message: String,
    val product_id: String,
    val promotion_id: Int,
    val status: Boolean
)