package agstack.gramophone.ui.login.model

import com.google.gson.annotations.SerializedName

class SendOtpRequestModel {

    @SerializedName("mobile_no")
    var phone: String? = null

    @SerializedName("language  ")
    var language  : String? = null

    @SerializedName("referral_code  ")
    var referral_code  : String? = null
}