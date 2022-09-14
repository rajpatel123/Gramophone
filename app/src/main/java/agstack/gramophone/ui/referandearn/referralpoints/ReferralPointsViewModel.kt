package agstack.gramophone.ui.referandearn.referralpoints

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.referandearn.aboutreferralpoints.AboutReferralPointsBalanceActivity
import agstack.gramophone.ui.referandearn.transaction.AllTransactionsListActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import agstack.gramophone.ui.referandearn.model.GpApiResponseData
import agstack.gramophone.ui.referandearn.model.ReferralPointsItem

@HiltViewModel
class ReferralPointsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<ReferralPointsNavigator>() {


    var gramCashResponseDataFromBundle = ObservableField<GpApiResponseData>()

    fun viewAllReferralTransactionClick() {
        getNavigator()?.openActivity(AllTransactionsListActivity::class.java,Bundle().apply {
            putParcelableArrayList(Constants.ReferralPointBalanceData,gramCashResponseDataFromBundle.get()?.referralPoints as ArrayList<ReferralPointsItem>)
        })

    }


    fun knowMoreReferralPointBalanceClick() {

        getNavigator()?.openActivity(AboutReferralPointsBalanceActivity::class.java, Bundle().apply {
            putParcelableArrayList(Constants.ReferralPointBalanceData,gramCashResponseDataFromBundle.get()?.referralPoints as ArrayList<ReferralPointsItem>)
        })

    }
}