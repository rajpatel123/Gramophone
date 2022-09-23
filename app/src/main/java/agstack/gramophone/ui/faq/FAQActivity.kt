package agstack.gramophone.ui.faq

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.FaqActivityBinding
import agstack.gramophone.ui.referandearn.model.GpApiResponseData
import agstack.gramophone.ui.referandearn.model.GramcashFaqItem
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import java.util.ResourceBundle.getBundle

@AndroidEntryPoint
class FAQActivity :
    BaseActivityWrapper<FaqActivityBinding, FAQNavigator, FAQViewModel>(),
    FAQNavigator {

    private val faqViewModel: FAQViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.frequently_asked_quest), R.drawable.ic_arrow_left)
        val bundle = getBundle()
        bundle?.let {
            if (bundle.getParcelableArrayList<GramcashFaqItem>(Constants.GramCashFAQList) != null) {
                mViewModel?.FAQList?.set(bundle.getParcelableArrayList(Constants.GramCashFAQList))
                mViewModel?.getFAQData()
            }
        }

    }

    fun getBundle(): Bundle? {
        return intent.extras
    }
    override fun getLayoutID(): Int {
        return R.layout.faq_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): FAQViewModel {
        return faqViewModel
    }

    override fun setFAQAdapter(faqAdapter: FAQAdapter) {
        viewDataBinding.rvFaq.adapter = faqAdapter
    }

}