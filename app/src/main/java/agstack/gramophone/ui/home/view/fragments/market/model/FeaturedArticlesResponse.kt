package agstack.gramophone.ui.home.view.fragments.market.model

import com.google.gson.annotations.SerializedName

class FeaturedArticlesResponse : ArrayList<FeaturedArticlesResponseItem>()

data class FeaturedArticlesResponseItem(
    val id: Int,
    val title: TitleX? = null,
    val min_to_read: String? = null,
    val post_views: String? = null,
    val acf: Acf? = null,
    val _embedded: Embedded? = null,
)

data class Embedded(
    @field:SerializedName("wp:featuredmedia")
    val featuredmedia: List<WpFeaturedmedia>? = null,

    )

data class Acf(
    val category_name: String? = null,
    val crop_name: String? = null,
    val featured_post_sorting: Int,
    val icon: Int,
)

data class TitleX(
    val rendered: String? = null,
)

data class WpFeaturedmedia(
    val media_details: MediaDetails? = null,
    val source_url: String? = null,
)

data class MediaDetails(
    val sizes: Sizes? = null,
)

data class Sizes(
    @field:SerializedName("yarpp-thumbnail")
    val thumbnail: YarppThumbnail? = null,
)

data class YarppThumbnail(
    val source_url: String? = null,
)
