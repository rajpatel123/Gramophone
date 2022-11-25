package agstack.gramophone.ui.search.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GlobalSearchRequest(
    var keyword: String? = null,
    var source: String? = null, // "app", "app-customer"
    var pageSection: String? = null, // "search", "suggestion"
    var skip: String? = "0",
    var limit: String? = "10",
    var afterKey : String? = "0"
) : Parcelable
