package agstack.gramophone.ui.home.view.fragments.market.model

import com.google.gson.annotations.SerializedName

class TrendingArticlesResponse : ArrayList<TrendingArticlesResponseItem>()

data class TrendingArticlesResponseItem(
    val id: Int,
    val title: TitleX? = null,
    val min_to_read: String? = null,
    val post_views: String? = null,
    val acf: Acf? = null,
    val _embedded: Embedded? = null,
)


