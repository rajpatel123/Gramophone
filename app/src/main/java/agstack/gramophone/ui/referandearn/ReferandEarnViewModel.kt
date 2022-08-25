package agstack.gramophone.ui.referandearn

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.faq.FAQActivity
import agstack.gramophone.ui.referandearn.referralpoints.ReferralPointsActivity
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReferandEarnViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<ReferandEarnNavigator>() {

    fun showReferralPointsActivity() {
        getNavigator()?.openActivity(ReferralPointsActivity::class.java)
    }

    fun onFAQClicked(){
        getNavigator()?.openActivity(FAQActivity::class.java)
    }

}