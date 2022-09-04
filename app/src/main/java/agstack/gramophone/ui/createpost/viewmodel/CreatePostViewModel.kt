package agstack.gramophone.ui.createpost.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.createpost.CreatePostNavigator
import android.view.View
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<CreatePostNavigator>(){

    fun openOrCapture(view: View){

        when(view.id){

            R.id.ivAddImage -> {

            }
            R.id.ivAddImageOne -> {

            }
            R.id.ivAddImageTwo -> {

            }

        }



    }


}