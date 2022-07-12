package agstack.gramophone.ui.verifyotp.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnboardingRepository
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyOtpViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : BaseViewModel<VerifyOTPNavigator>() {




}