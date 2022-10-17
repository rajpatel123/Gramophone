package agstack.gramophone.ui.search.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityGlobalSearchBinding
import agstack.gramophone.ui.search.adapter.SuggestionAdapter
import agstack.gramophone.ui.search.model.SuggestionsRequest
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import agstack.gramophone.ui.search.viewmodel.GlobalSearchViewModel
import agstack.gramophone.utils.RxSearchObservable
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class GlobalSearchActivity :
    BaseActivityWrapper<ActivityGlobalSearchBinding, GlobalSearchNavigator, GlobalSearchViewModel>(),
    GlobalSearchNavigator {
    val list = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        search()
        viewDataBinding.recyclerViewSearch.adapter = SuggestionAdapter(list) {}
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
    override fun notifyAdapter(suggestions: List<String>) {
        list.clear()
        list.addAll(suggestions)
        viewDataBinding.recyclerViewSearch.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressClick() {
        finishActivity()
    }

    override fun onClearSearchClick() {
        viewDataBinding.edtSearch.text?.clear()
    }

    fun search() {
        RxSearchObservable.fromView(viewDataBinding.edtSearch)
            .debounce(600, TimeUnit.MILLISECONDS)
            /*.filter { !it.isNullOrEmpty() }*/
            .map { s -> s.toString().lowercase(Locale.getDefault()).trim() }
            .distinctUntilChanged()
            .switchMap { Flowable.just(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { getViewModel().getSuggestions(SuggestionsRequest(it)) }
    }

}