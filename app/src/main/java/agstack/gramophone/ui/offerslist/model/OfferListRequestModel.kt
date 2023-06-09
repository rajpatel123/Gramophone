package agstack.gramophone.ui.offerslist.model

data class OfferListRequestModel (

    var limit: Int? = 10,
    var page:Int?=1,
    var requested_source: String? = null,
    var customer_id: String? = null,
    var business_type: String? = null,
    var language: String?=null,
    var search:String?=null
)