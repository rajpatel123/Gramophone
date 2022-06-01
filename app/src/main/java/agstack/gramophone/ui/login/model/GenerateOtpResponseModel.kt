package agstack.gramophone.ui.login.model

import com.google.gson.annotations.SerializedName
import agstack.gramophone.ui.login.model.ReferralAppliedModel
import java.io.Serializable

class GenerateOtpResponseModel : Serializable {
    @SerializedName("otp_sent")
    var otp_sent: Boolean? = null

    @SerializedName("referral")
    var referral: ReferralAppliedModel? = null
}