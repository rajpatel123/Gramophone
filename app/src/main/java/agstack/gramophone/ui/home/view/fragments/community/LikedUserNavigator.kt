package agstack.gramophone.ui.home.view.fragments.community

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.LikedUsersAdapter
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedUsers

interface LikedUserNavigator : BaseNavigator {
     fun updateUserList(likedUsersAdapter: LikedUsersAdapter, onLanguageClicked: (LikedUsers) -> Unit)
     fun setUpToolBar(size: Int)
}