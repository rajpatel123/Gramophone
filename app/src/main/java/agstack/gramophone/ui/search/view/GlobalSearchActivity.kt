package agstack.gramophone.ui.search.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityGlobalSearchBinding
import agstack.gramophone.ui.search.adapter.SearchResultAdapter
import agstack.gramophone.ui.search.adapter.SuggestionAdapter
import agstack.gramophone.ui.search.model.Data
import agstack.gramophone.ui.search.model.GlobalSearchRequest
import agstack.gramophone.ui.search.model.SuggestionsRequest
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import agstack.gramophone.ui.search.viewmodel.GlobalSearchViewModel
import agstack.gramophone.utils.RxSearchObservable
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.amnix.xtension.extensions.toCamelCase
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class GlobalSearchActivity :
    BaseActivityWrapper<ActivityGlobalSearchBinding, GlobalSearchNavigator, GlobalSearchViewModel>(),
    GlobalSearchNavigator {
    private val suggestionList = arrayListOf<String>()
    private val originalSearchResultList = arrayListOf<Data>()
    private val filterSearchResultList = arrayListOf<Data>()
    private var searchInCommunity = false
    private var disableSearchForAWhile = false
    private val handler =  Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchInCommunity = getBundle()?.getBoolean("searchInCommunity")!!

        searchSuggestions()

        viewDataBinding.recyclerViewSuggestions.adapter = SuggestionAdapter(suggestionList) {
            disableSearchForAWhile = true
            handler.postDelayed({
                disableSearchForAWhile = false
            }, 1500)

            hideSoftKeyboard(viewDataBinding.edtSearch)
            viewDataBinding.edtSearch.setText(it)
            viewDataBinding.edtSearch.setSelection(it.length)
            getViewModel().searchByKeyword(GlobalSearchRequest(keyword = it, source = "app", pageSection = "suggestion"), searchInCommunity)
        }
        viewDataBinding.recyclerViewSearchResult.adapter = SearchResultAdapter(filterSearchResultList) {
            showToast(it)
        }

        handler.postDelayed({
            viewDataBinding.edtSearch.requestFocus()
            showSoftKeyboard(viewDataBinding.edtSearch)
        }, 300)

        viewDataBinding.edtSearch.setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard(viewDataBinding.edtSearch)
                getViewModel().searchByKeyword(GlobalSearchRequest(viewDataBinding.edtSearch.text.toString(), source = "app", pageSection = "search"), searchInCommunity)
            }
            false
        }
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard(viewDataBinding.edtSearch)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_global_search
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): GlobalSearchViewModel {
        val viewModel: GlobalSearchViewModel by viewModels()
        return viewModel
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifySuggestionAdapter(suggestions: List<String>) {
        // clear and hide search result
        originalSearchResultList.clear()
        filterSearchResultList.clear()

        viewDataBinding.recyclerViewSearchResult.adapter?.notifyDataSetChanged()
        viewDataBinding.recyclerViewSearchResult.visibility = View.GONE
        removeTabs()

        suggestionList.clear()
        suggestionList.addAll(suggestions)
        viewDataBinding.recyclerViewSuggestions.adapter?.notifyDataSetChanged()

        if (suggestionList.size > 0) {
            viewDataBinding.suggestionsResultWrapper.visibility = View.VISIBLE
            viewDataBinding.emptyResultWrapper.visibility = View.GONE
        } else {
            viewDataBinding.suggestionsResultWrapper.visibility = View.GONE
            viewDataBinding.emptyResultWrapper.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifySearchResultAdapter(result: List<Data>) {
        //clear and hide suggestion list
        suggestionList.clear()
        viewDataBinding.recyclerViewSuggestions.adapter?.notifyDataSetChanged()
        viewDataBinding.suggestionsResultWrapper.visibility = View.GONE

        originalSearchResultList.clear()
        originalSearchResultList.addAll(result)

        filterSearchResultList.clear()
        filterSearchResultList.addAll(result)
        viewDataBinding.recyclerViewSearchResult.adapter?.notifyDataSetChanged()

        if (originalSearchResultList.size > 0) {
            viewDataBinding.recyclerViewSearchResult.visibility = View.VISIBLE
            viewDataBinding.emptyResultWrapper.visibility = View.GONE
            addTabs()
        } else {
            viewDataBinding.recyclerViewSearchResult.visibility = View.GONE
            viewDataBinding.emptyResultWrapper.visibility = View.VISIBLE
            removeTabs()
        }
    }

    override fun onBackPressClick() {
        finishActivity()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClearSearchClick() {
        viewDataBinding.edtSearch.text?.clear()

        //clear suggestion list
        suggestionList.clear()
        viewDataBinding.recyclerViewSuggestions.adapter?.notifyDataSetChanged()
        viewDataBinding.suggestionsResultWrapper.visibility = View.VISIBLE

        // clear and hide search result
        originalSearchResultList.clear()
        filterSearchResultList.clear()
        viewDataBinding.recyclerViewSearchResult.adapter?.notifyDataSetChanged()
        viewDataBinding.recyclerViewSearchResult.visibility = View.GONE
        removeTabs()
    }

    private fun searchSuggestions() {
        RxSearchObservable.fromView(viewDataBinding.edtSearch)
            .debounce(800, TimeUnit.MILLISECONDS)
            .filter { !it.isNullOrEmpty() && !disableSearchForAWhile}
            .map { s -> s.toString().lowercase(Locale.getDefault()).trim() }
           /* .distinctUntilChanged()*/
            .switchMap { Flowable.just(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { getViewModel().getSuggestions(SuggestionsRequest(it)) }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addTabs() {
        viewDataBinding.tabBarContainer.visibility = View.VISIBLE

        var tab = viewDataBinding.tabLayout.newTab()
        tab.tag = "All"
        tab.text = "All"
        viewDataBinding.tabLayout.addTab(tab)

        originalSearchResultList.forEach { item ->
            tab = viewDataBinding.tabLayout.newTab()
            tab.tag = item.type
            tab.text = item.type?.replace('_', ' ')?.toCamelCase()?.trim()
            viewDataBinding.tabLayout.addTab(tab)
        }

        viewDataBinding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if ("All" == tab?.tag) {
                    filterSearchResultList.clear()
                    filterSearchResultList.addAll(originalSearchResultList)
                    viewDataBinding.recyclerViewSearchResult.adapter?.notifyDataSetChanged()
                }else{
                    originalSearchResultList.filter { it.type == tab?.tag }.forEach {
                        filterSearchResultList.clear()
                        filterSearchResultList.add(it)
                        viewDataBinding.recyclerViewSearchResult.adapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun removeTabs(){
        viewDataBinding.tabLayout.removeAllTabs()
        viewDataBinding.tabBarContainer.visibility = View.GONE
    }
}