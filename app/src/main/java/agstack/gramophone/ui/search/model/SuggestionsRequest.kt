package agstack.gramophone.ui.search.model

data class SuggestionsRequest(
    var keyword: String? = null,
    var search_type : String? = "global",
)