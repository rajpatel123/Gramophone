package agstack.gramophone.ui.home.product.fragment

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RelatedProductViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : BaseViewModel<RelatedProductNavigator>() {
}
