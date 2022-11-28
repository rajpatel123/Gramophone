package agstack.gramophone.ui.search.view

import agstack.gramophone.R
import agstack.gramophone.databinding.ActivityViewAllSearchPostsBinding
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.postdetails.view.PostDetailsActivity
import agstack.gramophone.ui.search.adapter.PostsAdapter
import agstack.gramophone.ui.search.model.Data
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import agstack.gramophone.ui.search.viewmodel.GlobalSearchViewModel
import agstack.gramophone.utils.Constants
import android.annotation.SuppressLint
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllPostsActivity :
    GlobalSearchBaseActivity<ActivityViewAllSearchPostsBinding, GlobalSearchNavigator, GlobalSearchViewModel>(),
    GlobalSearchNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scrollChangeListener?.let { viewDataBinding.nestedScrollView.setOnScrollChangeListener(it) }

        viewDataBinding.recyclerViewPosts.adapter = PostsAdapter(
            displayDataList,
            listener = {
                openActivity(
                    this@ViewAllPostsActivity,
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

    override fun getLayoutID(): Int {
        return R.layout.activity_view_all_search_posts
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifySearchResultAdapter(result: List<Data>) {
        super.notifySearchResultAdapter(result)
        val startPosition = displayDataList.size - 1
        var itemCount = 0

        result.filter { it.type?.lowercase() == "posts" }.forEach {
            displayDataList.addAll(it.items)
            itemCount = it.items.size
        }

        try {
            viewDataBinding.recyclerViewPosts.adapter?.notifyItemRangeInserted(startPosition,
                itemCount)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}