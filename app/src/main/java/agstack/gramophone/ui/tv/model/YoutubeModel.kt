package agstack.gramophone.ui.tv.model

data class YoutubeModel(
    val etag: String,
    val items: List<YoutubeChannelItem>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo,
)

data class YoutubeChannelItem(
    val contentDetails: ContentDetailsModel,
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: SnippetModel,
)

data class PageInfo(
    val resultsPerPage: Int,
    val totalResults: Int,
)

data class ContentDetailsModel(
    val itemCount: Int,
    val videoId: String,
)

data class SnippetModel(
    val channelId: String,
    val channelTitle: String,
    val description: String,
    val localized: Localized,
    val publishedAt: String,
    val thumbnails: YoutubeThumbnailModel,
    val title: String,
)

data class Localized(
    val description: String,
    val title: String,
)

data class YoutubeThumbnailModel(
    val default: YoutubeDefaultThumbnail,
    val high: High,
    val maxres: Maxres,
    val medium: Medium,
    val standard: Standard,
)

data class YoutubeDefaultThumbnail(
    val height: Int,
    val url: String,
    val width: Int,
)

data class High(
    val height: Int,
    val url: String,
    val width: Int,
)

data class Maxres(
    val height: Int,
    val url: String,
    val width: Int,
)

data class Medium(
    val height: Int,
    val url: String,
    val width: Int,
)

data class Standard(
    val height: Int,
    val url: String,
    val width: Int,
)