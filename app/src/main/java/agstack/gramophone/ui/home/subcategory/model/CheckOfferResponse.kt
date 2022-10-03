package agstack.gramophone.ui.home.subcategory.model

data class CheckOfferResponse(
    val gp_api_error_data: List<Any>,
    val gp_api_exception: List<Any>,
    val gp_api_message: String,
    val gp_api_response_data: GpApiOfferResponse,
    val gp_api_status: String
)

data class GpApiOfferResponse(
    val benefits: Benefits,
    val message: String
)

data class Benefits(
    val cart_reference_id: String,
    val free_or_offer_product_discount_total: Int,
    val free_product: List<Any>,
    val order_details: List<OrderDetail>,
    val product_discount_total: Int,
    val promotional_discount_total: Int
)

data class OrderDetail(
    val benefits: BenefitsX,
    val best_promo_id: Int,
    val brand_name: String,
    val brand_name_id: Int,
    val cart_product_price: Int,
    val cart_reference_id: String,
    val cart_source: Any,
    val cgst_percent: String,
    val company_id: Any,
    val company_name: String,
    val cost_price: Any,
    val currency: String,
    val discount_percent: Int,
    val discount_price: String,
    val gramcash_redeemable: Boolean,
    val image: String,
    val item_product_discount: Int,
    val price_for_cart: String,
    val product_app_name: String,
    val product_base_name: String,
    val product_brand_name: String,
    val product_cost_price: Any,
    val product_currency: String,
    val product_details: ProductDetails,
    val product_featured_flag: Int,
    val product_id: String,
    val product_image: String,
    val product_images: List<String>,
    val product_name: String,
    val product_score: Int,
    val product_sku: String,
    val product_sku_list: List<ProductSku>,
    val product_type_id: String,
    val product_type_name: String,
    val product_unit: String,
    val promotinal_discount: Int,
    val promotion_details: PromotionDetails,
    val promotion_id: Int,
    val quantity: Int,
    val region_id: Int,
    val retailer_cart_type: Any,
    val retailer_trade_licence: Any,
    val sales_focus_for_customer: Int,
    val sales_focus_for_retailer: Int,
    val selling_price: String,
    val sgst_percent: String,
    val shipping_details: ShippingDetails,
    val technical_details: List<TechnicalDetail>,
    val unit_selling_price: String
)

data class BenefitsX(
    val discount_type: String,
    val per_unit_discount: String,
    val product_id: List<String>,
    val promotion_id: Int
)

data class ProductDetails(
    val detail: List<Detail>
)

data class ProductSku(
    val product_id: Int,
    val retailer_order_quantity_max: Int,
    val retailer_order_quantity_min: Int,
    val retailer_orderability: Boolean,
    val retailer_visibility: Boolean,
    val show_price_for_retailer: Boolean,
    val sku: String
)

data class PromotionDetails(
    val available_on_b2b_app: Boolean,
    val available_on_field_app: Boolean,
    val available_on_gs: Boolean,
    val available_on_gu: Boolean,
    val available_on_krishi_app: Boolean,
    val b2b_applicable: Boolean,
    val b2c_applicable: Boolean,
    val created_at: String,
    val created_by: Int,
    val deleted_at: Any,
    val end_date: Any,
    val id: Int,
    val image: String,
    val max_limit: Any,
    val name: String,
    val per_customer_limit: Any,
    val product_name: Any,
    val productsku: String,
    val promotion_type_id: Int,
    val promotion_used_count: Int,
    val start_date: String,
    val status: Boolean,
    val t_c: TC,
    val terms_conditions: String,
    val title: Title,
    val titleLang: String,
    val updated_at: String
)

data class ShippingDetails(
    val cash_on_delivery: String,
    val free_shipping: String,
    val shipping_cost: String
)

data class TechnicalDetail(
    val biological: Int,
    val code: Int,
    val compatibility_remark: String,
    val formulation_code: String,
    val how_it_works: String,
    val language: String,
    val mode_of_action: String,
    val physical_form: String,
    val potency: String,
    val special_attributes: String,
    val speed_of_action: String,
    val toxicity_label: String
)

data class Detail(
    val product_detail_key: String,
    val product_detail_type: String,
    val product_detail_value: String
)

data class TC(
    val en: String,
    val hi: String,
    val kn: String,
    val mr: String
)

data class Title(
    val en: String,
    val hi: String,
    val kn: String,
    val mr: String
)