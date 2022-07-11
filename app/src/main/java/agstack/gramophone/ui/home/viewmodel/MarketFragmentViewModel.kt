package agstack.gramophone.ui.home.viewmodel

import agstack.gramophone.data.repository.onboarding.OnBoardingRepository

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class MarketFragmentViewModel @Inject constructor(
    private val onboardingRepository: OnBoardingRepository

) : ViewModel() {


}