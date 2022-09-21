package agstack.gramophone.ui.referandearn.referralpoints

import agstack.gramophone.base.BaseNavigator

interface ReferralPointsNavigator :BaseNavigator {
     fun setMyReferralsAdapter(myReferralsAdapter: MyReferralsAdapter)
    fun openShareIntent()
}