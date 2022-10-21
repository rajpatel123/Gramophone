package agstack.gramophone.ui.home.view.fragments.gramophone.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.home.view.fragments.gramophone.MyGramophoneFragmentNavigator
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MyGramophoneFragmentViewModel
@Inject constructor(
    private val onboardingRepository: OnBoardingRepository
) : BaseViewModel<MyGramophoneFragmentNavigator>() {

    val tvName = ObservableField<String>()
    val tvLocation = ObservableField<String>()

    fun initProfile(){
       tvName.set(getNavigator()?.getUserName())
       tvLocation.set(getNavigator()?.getUserAddress())
       getNavigator()?.setUserImage()
    }




}