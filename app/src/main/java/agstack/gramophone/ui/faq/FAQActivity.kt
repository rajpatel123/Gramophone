package agstack.gramophone.ui.faq

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.FaqActivityBinding
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FAQActivity :
    BaseActivityWrapper<FaqActivityBinding, FAQNavigator, FAQViewModel>(),
    FAQNavigator {

    private val faqViewModel: FAQViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.frequently_asked_quest), R.drawable.ic_arrow_left)
        mViewModel?.getFAQData()
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