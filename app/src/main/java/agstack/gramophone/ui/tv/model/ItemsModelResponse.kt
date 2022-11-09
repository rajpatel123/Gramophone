package agstack.gramophone.ui.tv.model

data class ItemsModelResponse(
    val etag: String,
    val items: List<PlayListItemModels>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo,
)

data class PlayListItemModels(
    val contentDetails: ContentDetailsModel,
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: SnippetModel,
)
