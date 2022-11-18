package agstack.gramophone.ui.search.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.databinding.ActivityViewAllSearchPostsBinding
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikePostResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.postdetails.view.PostDetailsActivity
import agstack.gramophone.ui.search.adapter.PostsAdapter
import agstack.gramophone.ui.search.model.Data
import agstack.gramophone.ui.search.navigator.ViewAllSearchPostsNavigator
import agstack.gramophone.ui.search.viewmodel.ViewAllSearchPostsViewModel
import agstack.gramophone.utils.Constants
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.amnix.xtension.extensions.toCamelCase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllSearchPostsActivity :
    BaseActivityWrapper<ActivityViewAllSearchPostsBinding, ViewAllSearchPostsNavigator, ViewAllSearchPostsViewModel>(),
    ViewAllSearchPostsNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataList = getBundle()?.getParcelable<Data>("dataList")
        dataList?.type?.replace('_', ' ')?.toCamelCase()?.trim()?.let { setToolbarTitle(it) }

        val items = dataList?.items
        items?.let {
            viewDataBinding.recyclerViewPosts.adapter = PostsAdapter(
                items,
                listener = {
                    openActivity(
                        this@ViewAllSearchPostsActivity,
                        PostDetailsActivity::class.java,
                        Bundle().apply {
                            putString(Constants.POST_ID, it)
                        })
                },
                listener2 = {
                    getViewModel().likePost(PostRequestModel(it, ""))
                },
            )
        }
    }

    fun getBundle(): Bundle? {
        return intent.extras
    }

    fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_cross)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_view_all_search_posts
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun onLikePostSuccess(response: LikePostResponseModel?) {

    }

    override fun getViewModel(): ViewAllSearchPostsViewModel {
        val viewModel: ViewAllSearchPostsViewModel by viewModels()
        return viewModel
    }

    fun <T> openActivity(context: Context, cls: Class<T>, extras: Bundle?) {
        Intent(context, cls).apply {
            if (extras != null)
                putExtras(extras)
            context.startActivity(this)
        }
    }
}