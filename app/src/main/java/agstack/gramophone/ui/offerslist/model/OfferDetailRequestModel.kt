package agstack.gramophone.ui.offerslist.model

data class OfferDetailRequestModel(
    val language: String,
    val promotion_id: Int,
    val requested_source: String
)