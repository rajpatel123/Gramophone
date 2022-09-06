package agstack.gramophone.ui.home.view.fragments.community.model

import okhttp3.internal.immutableListOf

data class Data(
    var viewType: Int = 0,
    val textItem: String? = null,
    var isSelected: Boolean? = null,
    val pagerItemList: List<PagerItem> = immutableListOf()
)

data class PagerItem(
    val itemText: String,
    val itemImageUrl: String
)