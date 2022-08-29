package agstack.gramophone.ui.home.view.fragments.community.model

import okhttp3.internal.immutableListOf

data class Data(
    val textItem: String? = null,
    val pagerItemList: List<PagerItem> = immutableListOf()
)

data class PagerItem(
    val itemText: String,
    val itemImageUrl: String
)