package agstack.gramophone.ui.advisory.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.advisory.AllCropProblemNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CropProblemViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
):BaseViewModel<AllCropProblemNavigator>() {
}