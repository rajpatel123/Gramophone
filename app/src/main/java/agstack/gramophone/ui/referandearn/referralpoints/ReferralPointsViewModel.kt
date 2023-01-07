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
import agstack.gramophone.ui.referandearn.model.MyReferralsItem
import agstack.gramophone.ui.referandearn.model.ReferralPointsItem

@HiltViewModel
class ReferralPointsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<ReferralPointsNavigator>() {

    var myReferralsListSize = ObservableField<Int>(0)


    var gramCashResponseDataFromBundle = ObservableField<GpApiResponseData>()

    fun viewAllReferralTransactionClick() {
        getNavigator()?.sendMoEngageEvents("KA_Click_ViewReferralTransaction")
        getNavigator()?.openActivity(AllTransactionsListActivity::class.java)
    }


    fun knowMoreReferralPointBalanceClick() {
        getNavigator()?.sendMoEngageEvents("KA_Click_KnowMore")
        getNavigator()?.openActivity(AboutReferralPointsBalanceActivity::class.java, Bundle().apply {
            putParcelableArrayList(Constants.ReferralPointBalanceData,gramCashResponseDataFromBundle.get()?.referralPoints as ArrayList<ReferralPointsItem>)
        })

    }

    fun setMyReferralsListAdapter() {
        val myReferrals = gramCashResponseDataFromBundle.get()?.myReferrals!!
        if(myReferrals.size>0){

            myReferralsListSize.set(myReferrals.size)
            getNavigator()?.setMyReferralsAdapter(MyReferralsAdapter(myReferrals as ArrayList<MyReferralsItem>))
        }
    }


    fun onReferFriendClicked(){
        getNavigator()?.openShareIntent()

    }
}