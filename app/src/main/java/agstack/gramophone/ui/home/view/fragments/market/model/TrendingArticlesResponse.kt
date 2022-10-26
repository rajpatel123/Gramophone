package agstack.gramophone.ui.home.view.fragments.market.model

class TrendingArticlesResponse : ArrayList<TrendingArticlesResponseItem>()

data class TrendingArticlesResponseItem(
    val id: Int,
    val title: TitleX? = null,
    val min_to_read: String? = null,
    val post_views: String? = null,
    val acf: Any? = null,
    val featured_image: String? = null,
)