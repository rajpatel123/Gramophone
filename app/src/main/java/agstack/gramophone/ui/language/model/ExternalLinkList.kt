package agstack.gramophone.ui.language.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExternalLinkList(
    @SerializedName("privacy_policy_url")
    val privacy_policy_url: String,

    @SerializedName("referral_terms_conditions")
    val referral_terms_conditions: String,

    @SerializedName("terms_of_service_url")
    val terms_of_service_url: String,

    @SerializedName("app_features_url")
    val app_features_url: String
):Parcelable