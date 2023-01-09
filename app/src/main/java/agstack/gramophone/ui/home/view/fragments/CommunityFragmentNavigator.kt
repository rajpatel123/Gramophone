package agstack.gramophone.ui.home.view.fragments

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.CommunityPostAdapter
import agstack.gramophone.ui.home.view.fragments.community.model.quiz.Option
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.Data

interface CommunityFragmentNavigator: BaseNavigator {
    fun updatePostList(
        postList: CommunityPostAdapter,
        sharePoll: (type:String) -> Unit,
        quizPollAnswered: (option: Option) -> Unit,
        postDetailClicked: (postId: String) -> Unit,
        onItemLikesClicked: (postId: String) -> Unit,
        onItemCommentsClicked: (postId: String) -> Unit,
        onWhatsAppClicked: (postId: String) -> Unit,
        onTripleDotMenuClicked: (postId:String) -> Unit,
        onMenuOptionClicked: (post:Data) -> Unit,
        onLikeClicked: (post: Data) -> Unit,
        onBookMarkClicked: (post: Data) -> Unit,
        onFollowClicked: (post: Data) -> Unit,
        onProfileImageClicked: (post: Data) -> Unit
    )

    fun sharePost(it: String)
    fun deletePostDialog()
    fun reportPostDialog()
    fun blockUserDialog()
    fun getReportReason(): String
    fun setProfileImage(url:String)
    fun stopRefresh()
    fun hideViews()
    fun selecttab(i: Int)
}