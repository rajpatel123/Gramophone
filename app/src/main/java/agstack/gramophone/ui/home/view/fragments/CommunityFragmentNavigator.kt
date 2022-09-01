package agstack.gramophone.ui.home.view.fragments

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.CommunityPostAdapter
import agstack.gramophone.ui.home.view.fragments.community.model.Data

interface CommunityFragmentNavigator: BaseNavigator {
    fun updatePostList(postList: CommunityPostAdapter, post: () -> Unit)
}