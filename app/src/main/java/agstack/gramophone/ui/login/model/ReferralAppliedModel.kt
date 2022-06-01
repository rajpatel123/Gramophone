package agstack.gramophone.ui.login.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ReferralAppliedModel : Serializable {
    @SerializedName("valid")
    var referral_applied: Boolean? = null

    @SerializedName("message")
    var referral_message: String? = null
}