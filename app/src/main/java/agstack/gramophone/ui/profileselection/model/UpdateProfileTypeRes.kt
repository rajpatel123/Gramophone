package agstack.gramophone.ui.profileselection.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UpdateProfileTypeRes : Serializable {
    @SerializedName("data")
    var data: Boolean? = null
}