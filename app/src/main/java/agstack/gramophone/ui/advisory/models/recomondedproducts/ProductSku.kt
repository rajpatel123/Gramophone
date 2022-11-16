package agstack.gramophone.ui.advisory.models.recomondedproducts

data class ProductSku(
    val brand_name: String,
    val cost_price: Any,
    val currency: String,
    val discount_price: String,
    val id: String,
    val image: String,
    val name: String,
    val product_base_name: String
)