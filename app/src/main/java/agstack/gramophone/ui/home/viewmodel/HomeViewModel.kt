package agstack.gramophone.ui.home.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.data.repository.onboarding.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
    ) : BaseViewModel<HomeActivityNavigator>() {

}