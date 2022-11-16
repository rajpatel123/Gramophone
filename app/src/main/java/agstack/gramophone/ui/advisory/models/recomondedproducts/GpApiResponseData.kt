package agstack.gramophone.ui.advisory.models.recomondedproducts

data class GpApiResponseData(
    val product_base_name: String,
    val product_sku_list: List<ProductSku>
)