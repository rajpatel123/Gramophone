package agstack.gramophone.ui.language.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiResponseData(
    @SerializedName("current_version_code")
    val current_version_code: Int,

    @SerializedName("external_link_list")
    val external_link_list: ExternalLinkList,

    @SerializedName("help_data_list")
    val help_data_list: HelpDataList,

    @SerializedName("is_force_update_app")
    val is_force_update_app: String,

    @SerializedName("is_latest_version")
    val is_latest_version: String,

    @SerializedName("login_banner_list")
    val login_banner_list: List<LoginBanner>,

    @SerializedName("temp_token")
    val temp_token: String,

    @SerializedName("update_message")
    val update_message: String,

    @SerializedName("notifi_perm_ask_days")
    val notifi_perm_ask_days: Int,

    @SerializedName("notifi_max_attempts")
    val notifi_max_attempts: Int,

    @SerializedName("notifi_messages")
    val notifi_messages: String,

):Parcelable