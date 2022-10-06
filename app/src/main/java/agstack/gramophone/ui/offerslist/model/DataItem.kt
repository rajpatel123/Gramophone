package agstack.gramophone.ui.offerslist.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataItem(

	@field:SerializedName("max_limit")
	val maxLimit: Int? = null,

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("titleLang")
	val titleLang: String? = null,

	@field:SerializedName("promotion_id")
	val promotionId: Int? = null,

	@field:SerializedName("rules")
	val rules: String? = null,

	@field:SerializedName("title")
	val title: Title? = null,

	@field:SerializedName("b2c_applicable")
	val b2cApplicable: Boolean? = null,

	@field:SerializedName("promotion_type_id")
	val promotionTypeId: Int? = null,

	@field:SerializedName("product_base_name")
	val productBaseName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("terms_conditions")
	val termsConditions: String? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("t_c")
	val tC: TC? = null,

	@field:SerializedName("per_customer_limit")
	val perCustomerLimit: Int? = null,

	@field:SerializedName("promotion_subtype_id")
	val promotionSubtypeId: Int? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("available_on_krishi_app")
	val availableOnKrishiApp: Boolean? = null,

	@field:SerializedName("created_by")
	val createdBy: Int? = null,

	@field:SerializedName("product_name")
	val productName: String? = null,

	@field:SerializedName("productsku")
	val productsku: String? = null,

	@field:SerializedName("available_on_gs")
	val availableOnGs: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("b2b_applicable")
	val b2bApplicable: Boolean? = null,

	@field:SerializedName("available_on_b2b_app")
	val availableOnB2bApp: Boolean? = null,

	@field:SerializedName("available_on_field_app")
	val availableOnFieldApp: Boolean? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
) : Parcelable