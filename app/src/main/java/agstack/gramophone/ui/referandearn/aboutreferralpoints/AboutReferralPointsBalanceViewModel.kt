package agstack.gramophone.ui.referandearn.aboutreferralpoints

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutReferralPointsBalanceViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<AboutReferralPointsBalanceNavigator>() {


}