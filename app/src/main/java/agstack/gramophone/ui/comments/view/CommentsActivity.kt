package agstack.gramophone.ui.comments.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCommentsBinding
import agstack.gramophone.ui.comments.CommentNavigator
import agstack.gramophone.ui.comments.viewModel.CommentsViewModel
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.utils.Constants.POST_ID
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_comments.*

@AndroidEntryPoint
class CommentsActivity :
    BaseActivityWrapper<ActivityCommentsBinding, CommentNavigator, CommentsViewModel>(),
    CommentNavigator {
    private val commentsViewModel: CommentsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        commentsViewModel.getComments(intent.extras?.getString(POST_ID)!!)
        tvCommentBottom.requestFocus()
        val imm :InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
        imm.showSoftInput(tvCommentBottom, InputMethodManager.SHOW_IMPLICIT);
    }



    override fun getLayoutID(): Int = R.layout.activity_comments

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): CommentsViewModel = commentsViewModel


    override fun updateCommentsList(
        commentsAdapter: CommentsAdapter,
        onItemCommentsClicked: (commentId: String) -> Unit
    ) {
        commentsAdapter.onItemCommentsClicked = onItemCommentsClicked
        rvCommentsDialog?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCommentsDialog?.setHasFixedSize(false)
        rvCommentsDialog?.adapter = commentsAdapter
    }

    override fun finishActivity() {
        finish()
    }


}