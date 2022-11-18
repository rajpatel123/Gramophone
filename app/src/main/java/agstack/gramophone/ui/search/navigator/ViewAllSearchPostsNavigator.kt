package agstack.gramophone.ui.search.navigator

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikePostResponseModel

interface ViewAllSearchPostsNavigator : BaseNavigator {
    fun onLikePostSuccess(response: LikePostResponseModel?)
}