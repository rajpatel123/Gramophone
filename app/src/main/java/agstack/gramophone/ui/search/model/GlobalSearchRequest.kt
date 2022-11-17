package agstack.gramophone.ui.search.model

data class GlobalSearchRequest(
    var keyword: String? = null,
    var source: String? = null, // "app", "app-customer"
    var pageSection: String? = null, // "search", "suggestion"
)
