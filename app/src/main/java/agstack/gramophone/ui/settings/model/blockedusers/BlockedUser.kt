package agstack.gramophone.ui.settings.model.blockedusers

data class BlockedUser(
    val address: String,
    val customer_id: Int,
    val name: String,
    val profile_image: String
)