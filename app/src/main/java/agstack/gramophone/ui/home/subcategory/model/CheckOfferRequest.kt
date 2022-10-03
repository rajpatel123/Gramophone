package agstack.gramophone.ui.home.subcategory.model

data class CheckOfferRequest(
    val app_type: String,
    val business_type: String,
    val customer_id: String,
    val es_source: String,
    val products: List<OfferForProduct>,
    val source: String
)

data class OfferForProduct(
    val product_id: String,
    val promotion_id: Int,
    val quantity: Int,
    val selling_price: String
)