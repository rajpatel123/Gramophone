package agstack.gramophone.ui.gramcash.expiringsoon


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.GramcashexpiringsoonActivityBinding
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GCExpiringSoonActivity :
    BaseActivityWrapper<GramcashexpiringsoonActivityBinding, GCExpiringSoonNavigator, GCExpiringSoonViewModel>(),
    GCExpiringSoonNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.points_expiring), R.drawable.ic_arrow_left)
        mViewModel?.getGramCashTxnExpiringSoon()
    }

    private val gcExpiringSoonViewModel: GCExpiringSoonViewModel by viewModels()
    override fun getLayoutID(): Int {
        return R.layout.gramcashexpiringsoon_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): GCExpiringSoonViewModel {
        return gcExpiringSoonViewModel
    }


}
