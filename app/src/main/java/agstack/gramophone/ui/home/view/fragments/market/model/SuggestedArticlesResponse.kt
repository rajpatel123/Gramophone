package agstack.gramophone.ui.home.view.fragments.market.model

class SuggestedArticlesResponse : ArrayList<SuggestedArticlesResponseItem>()

data class SuggestedArticlesResponseItem(
    val ID: Int,
    val post_title: String? = null,
    val min_to_read: String? = null,
    val post_views: String? = null,
    val acf: Any? = null,
    val featured_image: String? = null,
)