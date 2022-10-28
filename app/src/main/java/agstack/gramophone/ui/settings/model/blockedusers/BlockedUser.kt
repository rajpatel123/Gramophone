package agstack.gramophone.ui.settings.model.blockedusers

import agstack.gramophone.ui.settings.model.blockmodels.Location

data class BlockedUser(
    val __v: Int,
    val _id: String,
    val address: String,
    val androidId: String,
    val badge: Any,
    val communityUserType: String,
    val createdDate: Long,
    val email: Any,
    val firebaseId: String,
    val firm: Any,
    val handle: String,
    val language: String,
    val location: Location,
    val location_name: String,
    val mandi: Any,
    val phoneNo: String,
    val photoUrl: String,
    val status: Int,
    val totalFollowees: Int,
    val totalFollowers: Int,
    val updatedAt: Long,
    val username: String,
    val uuid: String
)