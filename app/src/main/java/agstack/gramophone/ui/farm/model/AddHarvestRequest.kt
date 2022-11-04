package agstack.gramophone.ui.farm.model

data class AddHarvestRequest(
    val farm_reference_id: String,
    val output_qty: Int,
    val output_unit_id: String
)