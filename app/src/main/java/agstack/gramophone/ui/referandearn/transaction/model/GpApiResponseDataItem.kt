package agstack.gramophone.ui.referandearn.transaction.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiResponseDataItem(

	@field:SerializedName("transaction_date")
	val transactionDate: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("void")
	val jsonMemberVoid: Int? = null,

	@field:SerializedName("expiry_soon")
	val expirySoon: Boolean? = null,

	@field:SerializedName("expiry_date")
	val expiryDate: String? = null,

	@field:SerializedName("pending")
	val pending: Boolean? = null,

	@field:SerializedName("ledger_type")
	val ledgerType: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("gramcash_id")
	val gramcashId: Int? = null,

	@field:SerializedName("source")
	val source: String? = null,

	@field:SerializedName("transaction_type")
	val transactionType: String? = null,

	@field:SerializedName("trigger_event")
	val triggerEvent: String? = null,

	@field:SerializedName("is_expired")
	val isExpired: Boolean? = null,

	@field:SerializedName("trigger_event_name")
	val triggerEventName: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable