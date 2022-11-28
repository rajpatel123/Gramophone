package agstack.gramophone.ui.search.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikePostResponseModel
import agstack.gramophone.ui.search.model.Data
import agstack.gramophone.ui.search.model.GlobalSearchRequest
import agstack.gramophone.ui.search.model.Item
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import agstack.gramophone.ui.search.viewmodel.GlobalSearchViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.databinding.ViewDataBinding
import com.amnix.xtension.extensions.toCamelCase

abstract class GlobalSearchBaseActivity<B : ViewDataBinding, N : GlobalSearchNavigator, V : GlobalSearchViewModel> :
    BaseActivityWrapper<B, GlobalSearchNavigator, GlobalSearchViewModel>(),
    GlobalSearchNavigator {

    val displayDataList = arrayListOf<Item>()
    private var lastSearchRequest: GlobalSearchRequest? = null
    private var isSearchInCommunity: Boolean? = false
    private var loadingComplete: Boolean = false
    private var skip: Int = 0
    var scrollChangeListener : NestedScrollView.OnScrollChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lastSearchRequest = getBundle()?.getParcelable("lastSearchRequest")
        isSearchInCommunity = getBundle()?.getBoolean("isSearchInCommunity")

        val dataList = getBundle()?.getParcelable<Data>("dataList")
        dataList?.type?.replace('_', ' ')?.toCamelCase()?.trim()?.let { setToolbarTitle(it) }

        val items = dataList?.items
        items?.let { displayDataList.addAll(items as ArrayList<Item>) }

        skip = 0

        lastSearchRequest?.let {
            it.skip = skip.toString()
            it.afterKey = skip.toString()
        }

        scrollChangeListener =
            NestedScrollView.OnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                if (!loadingComplete && scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    skip += 10
                    lastSearchRequest?.let {
                        it.skip = skip.toString()
                        it.afterKey = skip.toString()
                        getViewModel().searchByKeyword(it, isSearchInCommunity == true)
                    }
                }
            }
    }

    fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_cross)
    }

    fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): GlobalSearchViewModel {
        val viewModel: GlobalSearchViewModel by viewModels()
        return viewModel
    }

    fun <T> openActivity(context: Context, cls: Class<T>, extras: Bundle?) {
        Intent(context, cls).apply {
            if (extras != null)
                putExtras(extras)
            context.startActivity(this)
        }
    }

    override fun notifySearchResultAdapter(result: List<Data>) {
        if(result.isEmpty()){
            loadingComplete = true
            return
        }

        loadingComplete = false


    }

    override fun notifySuggestionAdapter(suggestions: List<String>) {

    }

    override fun onBackPressClick() {

    }

    override fun onClearSearchClick() {

    }

    override fun onLikePostSuccess(response: LikePostResponseModel?) {

    }
}