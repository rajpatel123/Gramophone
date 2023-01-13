package agstack.gramophone.ui.bookmarked

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.tv.model.Bookmark

interface BookmarkedVideosNavigator : BaseNavigator {
    fun setBookmarkedAdapter(
        adapter: BookmarkedVideosAdapter,
        onVideoClicked: (videoID: String) -> Unit,
        onRemoveClick: (bookmarkedVideo: Bookmark) -> Unit,
    )
}
