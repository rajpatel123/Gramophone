package agstack.gramophone.ui.address.model.addressdetails

import agstack.gramophone.ui.address.model.State
import java.util.ArrayList

data class GpApiResponseData(
    val district: String,
    val pincode_list: ArrayList<PinCode>?,
    val state: String,
    val tehsil: String,
    val village: String
)