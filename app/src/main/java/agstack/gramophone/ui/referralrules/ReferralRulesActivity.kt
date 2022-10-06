package agstack.gramophone.ui.referralrules


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.FaqActivityBinding
import agstack.gramophone.databinding.ReferralRulesActivityBinding
import agstack.gramophone.ui.faq.FAQAdapter
import agstack.gramophone.ui.faq.FAQNavigator
import agstack.gramophone.ui.faq.FAQViewModel
import agstack.gramophone.ui.referandearn.model.GpApiResponseData
import agstack.gramophone.ui.referandearn.model.GramcashFaqItem
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import java.util.ResourceBundle.getBundle

@AndroidEntryPoint
class ReferralRulesActivity :
    BaseActivityWrapper<ReferralRulesActivityBinding, ReferralRulesNavigator, ReferralRulesViewModel>(),
    ReferralRulesNavigator {

    private val referralRulesViewModel: ReferralRulesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.referral_rules), R.drawable.ic_arrow_left)
        val bundle = getBundle()
        bundle?.let {
            if (bundle.getStringArrayList(Constants.GramCashReferralRulesList) != null) {
                mViewModel?.referralRulesList?.set(bundle.getStringArrayList(Constants.GramCashReferralRulesList))
                mViewModel?.getReferalRulesData()
            }
        }

    }

    fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun getLayoutID(): Int {
        return R.layout.referral_rules_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): ReferralRulesViewModel {
        return referralRulesViewModel
    }


    override fun setReferalRulesAdapter(referalRulesAdapter: ReferalRulesAdapter) {
        viewDataBinding.rvReferal.adapter = referalRulesAdapter
    }

}