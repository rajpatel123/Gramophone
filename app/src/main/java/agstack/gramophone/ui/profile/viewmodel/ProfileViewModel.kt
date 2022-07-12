package agstack.gramophone.ui.profile.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.profile.ProfileActivityNavigator
import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
) : BaseViewModel<ProfileActivityNavigator>() {

    fun logout(v: View){
        getNavigator()?.logout()
    }


}