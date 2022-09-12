package agstack.gramophone.ui.userprofile.model


data class UpdateProfileModel(

    var is_farmer: Boolean? = null,
    var is_trader: Boolean? = null,
    var firm_name: String? = null,
    var first_name: String? = null,
    var last_name: String? = null
)
