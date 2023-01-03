package agstack.gramophone.ui.bookmarked


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityBookmarkedVideosListBinding
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookmarkedVideosActivity :
    BaseActivityWrapper<ActivityBookmarkedVideosListBinding, BookmarkedVideosNavigator, BookmarkedVideosViewModel>(),
    BookmarkedVideosNavigator, View.OnClickListener {

    //initialise ViewModel
    private val bookmarkedVideosViewModel: BookmarkedVideosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        bookmarkedVideosViewModel.getBookmarkedVideoList()
    }

    private fun setupUi() {
        setUpToolBar(true, "Favorite Video", R.drawable.ic_arrow_left)
        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            bookmarkedVideosViewModel.getBookmarkedVideoList()
            viewDataBinding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.itemOrder -> {

            }
        }
    }

    override fun setBookmarkedAdapter(adapter: BookmarkedVideosAdapter) {
        viewDataBinding.rvBookmarkedVideos.adapter = adapter
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_bookmarked_videos_list
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): BookmarkedVideosViewModel {
        return bookmarkedVideosViewModel
    }

}