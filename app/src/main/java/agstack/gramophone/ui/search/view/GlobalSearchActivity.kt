package agstack.gramophone.ui.search.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityGlobalSearchBinding
import agstack.gramophone.ui.farm.viewmodel.WhyAddFarmViewModel
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import agstack.gramophone.ui.search.viewmodel.GlobalSearchViewModel
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GlobalSearchActivity : BaseActivityWrapper<ActivityGlobalSearchBinding, GlobalSearchNavigator, GlobalSearchViewModel>(),
    GlobalSearchNavigator {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}