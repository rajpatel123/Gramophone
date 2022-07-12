package agstack.gramophone.ui.home.view.fragments.profile.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.home.view.fragments.profile.ProfileFragmentNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileFragmentViewModel
@Inject constructor(
    private val onboardingRepository: OnBoardingRepository
) : BaseViewModel<ProfileFragmentNavigator>() {

}