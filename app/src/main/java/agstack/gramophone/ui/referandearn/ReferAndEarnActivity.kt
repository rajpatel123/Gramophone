package agstack.gramophone.ui.referandearn

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.EditProfileActivityBinding
import agstack.gramophone.databinding.ReferEarnActivityBinding
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferAndEarnActivity :
    BaseActivityWrapper<ReferEarnActivityBinding, ReferandEarnNavigator, ReferandEarnViewModel>(),
    ReferandEarnNavigator {

    private val referandEarnViewModel: ReferandEarnViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.refer_n_earn), R.drawable.ic_arrow_left)
    }
    override fun getLayoutID(): Int {
        return R.layout.refer_earn_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): ReferandEarnViewModel {
        return referandEarnViewModel
    }

}