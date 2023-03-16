package agstack.gramophone.ui.search.model

data class SuggestionsRequest(
    var keyword: String? = "",
    var search_type : String? = "global",
)