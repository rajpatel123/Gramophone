package agstack.gramophone.ui.userprofile.model

data class UserModel(


    var uuid: String? = null,

    var authorId: String? = null,

    val phoneNo: String? = null,
    val email: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val community_id: String? = null,
    val badge: String? = null,
    val geolocation: String? = null,
    val profile_image: String? = null,

    val address: String? = null,

    val username: String? = null,

    val photoUrl: String? = null,

    val location: UserLocation? = null,

    val location_name: String? = null,

    val language: String? = null,

    val following: Boolean = false,

    val count: Int =
        0,

    val totalFollowers: Int =
        0,

    val totalFollowees: Int =
        0,

    val app_demo_video_id: String? =
        null,

    val gramcash: String? =
        null,

    val handle: String? =
        null,

    val call_button: Boolean? =
        null,

    val address_exists: Boolean? =
        null,

    val crops_exists: Boolean? =
        null

)
