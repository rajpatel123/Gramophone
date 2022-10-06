package agstack.gramophone.ui.referralrules

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.faq.FAQAdapter
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReferralRulesViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<ReferralRulesNavigator>() {


    var referralRulesList = ObservableField<ArrayList<String>>()


    fun getReferalRulesData() {
        getNavigator()?.setReferalRulesAdapter(ReferalRulesAdapter(referralRulesList.get()!!))
    }




}