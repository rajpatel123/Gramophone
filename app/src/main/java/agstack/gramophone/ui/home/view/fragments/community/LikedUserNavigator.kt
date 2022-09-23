package agstack.gramophone.ui.home.view.fragments.community

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.LikedUsersAdapter
import agstack.gramophone.ui.home.view.fragments.community.model.likes.DataX
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedUsers
import android.widget.ImageView

interface LikedUserNavigator : BaseNavigator {
     fun updateUserList(likedUsersAdapter: LikedUsersAdapter, onLanguageClicked: (DataX) -> Unit)
     fun setUpToolBar(size: Int)
     fun onImageSet(imageUrl: String, iv: ImageView)
}