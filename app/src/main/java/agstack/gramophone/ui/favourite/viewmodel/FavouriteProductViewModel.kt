package agstack.gramophone.ui.favourite.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.favourite.FavouriteProductNavigator
import androidx.databinding.ObservableField
import javax.inject.Inject

class FavouriteProductViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
):BaseViewModel<FavouriteProductNavigator>() {

    val progress = ObservableField<Boolean>(false)

}