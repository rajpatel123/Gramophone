package agstack.gramophone.ui.postdetails.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityPostDetailsBinding
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.ui.postdetails.PostDetailNavigator
import agstack.gramophone.ui.postdetails.viewmodel.PostDetailViewModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.IntentKeys
import agstack.gramophone.utils.ShareSheetPresenter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_post_details.*

@AndroidEntryPoint
class PostDetailsActivity : BaseActivityWrapper<ActivityPostDetailsBinding,PostDetailNavigator,PostDetailViewModel>(),PostDetailNavigator {

    private val postDetailViewModel: PostDetailViewModel by viewModels()
    private var shareSheetPresenter: ShareSheetPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.getString(Constants.POST_ID)?.let { postDetailViewModel.getPostDetails(it) }
        intent.extras?.getString(Constants.POST_ID)?.let { postDetailViewModel.getPostComments(it) }
        setUpToolBar(
            enableBackButton = true,
            "",
            R.drawable.ic_arrow_left
        )

        shareSheetPresenter = this?.let { ShareSheetPresenter(this) }
    }


    override fun getLayoutID(): Int = R.layout.activity_post_details

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): PostDetailViewModel = postDetailViewModel
    override fun updatePostList(
        comments: CommentsAdapter,
        postDetailClicked: (commentId: String) -> Unit
    ) {

        rvComments.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvComments.setHasFixedSize(false)
        rvComments.adapter = comments
    }

    override fun onImageSet(url: String) {
        Glide.with(this).load(url).into(postImage)
    }

    override fun setLikeImage(icLiked: Int) {
        ivLike.setImageResource(icLiked)
    }

    override fun setBookMarkImage(icLiked: Int) {
        ivBookmark.setImageResource(icLiked)
    }

    override fun sharePost(link: String) {

        val shareMessage = resources.getString(R.string.welcome_msg)
        //  val extraText = shareMessage + "\n" + genericUri.toString()
        val extraText = shareMessage
        shareSheetPresenter?.shareDeepLinkWithExtraTextWithOption(
            extraText,
            getString(R.string.home_share_subject),
            IntentKeys.WhatsAppShareKey
        )

    }

}