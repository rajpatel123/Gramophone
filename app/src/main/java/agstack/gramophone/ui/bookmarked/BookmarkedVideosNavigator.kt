package agstack.gramophone.ui.bookmarked

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter

interface BookmarkedVideosNavigator : BaseNavigator {
    fun setBookmarkedAdapter(adapter: BookmarkedVideosAdapter, onVideoClicked: (videoID: String) -> Unit,)
}
