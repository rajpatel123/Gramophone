package agstack.gramophone.ui.home.view.fragments.gramophone.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.home.view.fragments.gramophone.ProfileFragmentNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileFragmentViewModel
@Inject constructor(
    private val onboardingRepository: OnBoardingRepository
) : BaseViewModel<ProfileFragmentNavigator>() {

}