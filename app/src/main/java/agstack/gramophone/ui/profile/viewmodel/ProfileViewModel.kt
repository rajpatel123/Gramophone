package agstack.gramophone.ui.profile.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.profile.ProfileActivityNavigator
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
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
        SharedPreferencesHelper.instance?.putBoolean(
            SharedPreferencesKeys.logged_in,
            false
        )
        getNavigator()?.logout()
    }


}