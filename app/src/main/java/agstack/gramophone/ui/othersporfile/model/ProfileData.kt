package agstack.gramophone.ui.othersporfile.model

data class ProfileData(
    val __v: Int,
    val _id: String,
    val address: String,
    val badge: Any,
    val communityUserType: String,
    val email: String,
    var following: Boolean,
    val handle: String,
    val language: String,
    val location: Location,
    val location_name: String,
    val notificationTokens: NotificationTokens,
    val phoneNo: String,
    val photoUrl: String,
    val totalFollowees: Int,
    val totalFollowers: Int,
    val username: String,
    val uuid: String,
    val firm: String?="--",
    val mandi: String?="--"

)