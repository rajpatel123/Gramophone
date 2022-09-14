package agstack.gramophone.ui.referandearn.aboutreferralpoints

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.referandearn.aboutreferralpoints.adapter.AboutReferalPointsAdapter

interface AboutReferralPointsBalanceNavigator :BaseNavigator {
     fun setAboutReferalPointsAdapter(aboutReferalPointsAdapter: AboutReferalPointsAdapter)
}