package agstack.gramophone.ui.advisory.models.advisory

data class LinkedTechnical(
    val can_be_purchased_from_outside: Boolean,
    val is_product_available: Boolean,
    val product_base_name: String,
    val product_composition: String,
    val product_images: String,
    val product_mrp_price: Float,
    val product_sale_price: Float
)