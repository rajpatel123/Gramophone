package agstack.gramophone.ui.farm.model


data class AddFarmRequest(
    var field_name: String? = null,
    var crop_id: String? = null,
    var area: Double? = null,
    var crop_sowing_date: String? = null,
    var unit: String? = null,
    var unit_id: String? = null,
)
