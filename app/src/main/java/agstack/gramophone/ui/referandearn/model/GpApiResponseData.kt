package agstack.gramophone.ui.referandearn.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiResponseData(

	@field:SerializedName("gramcash_rules")
	val gramcashRules: List<GramcashRulesItem?>? = null,

	@field:SerializedName("gramcash_total")
	val gramcashTotal: Int? = null,

	@field:SerializedName("referral_points")
	val referralPoints: List<ReferralPointsItem?>? = null,

	@field:SerializedName("referral_rules")
	val referralRules: List<String?>? = null,

	@field:SerializedName("gramcash_expiring_soon")
	val gramcashExpiringSoon: Int? = null,

	@field:SerializedName("gramcash_pending")
	val gramcashPending: Int? = null,

	@field:SerializedName("gramcash_expiring_soon_days")
	val gramcashExpiringSoonDays: Int? = null,

	@field:SerializedName("gramcash_faq")
	val gramcashFaq: List<GramcashFaqItem?>? = null,

	@field:SerializedName("referral_faq")
	val referralFaq: List<ReferralFaqItem?>? = null,

	@field:SerializedName("my_referrals")
	val myReferrals: List<MyReferralsItem?>? = null,

	@field:SerializedName("gramcash_available")
	val gramcashAvailable: Int? = null,

	@field:SerializedName("referral_code")
	val referral_code: String? = null,
	@field:SerializedName("share_message")
	val share_message: String? = null,


) : Parcelable