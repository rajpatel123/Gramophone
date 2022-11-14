package agstack.gramophone.ui.followings.model

data class Data(
    val _id: String,
    val badge: String,
    val following: Boolean,
    val photoUrl: String,
    val username: String,
    val uuid: String,
    val address_short:String,
    val blockStatus:Int,

)