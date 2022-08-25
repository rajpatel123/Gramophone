package agstack.gramophone.ui.referandearn.referralpoints

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.referandearn.transaction.AllTransactionsListActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReferralPointsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<ReferralPointsNavigator>() {

    fun viewAllReferralTransactionClick() {
        getNavigator()?.openActivity(AllTransactionsListActivity::class.java)

    }


    fun knowMoreReferralPointBalanceClick() {

    }
}