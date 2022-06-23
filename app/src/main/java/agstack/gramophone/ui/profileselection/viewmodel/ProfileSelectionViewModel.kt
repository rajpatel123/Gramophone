package agstack.gramophone.ui.profileselection.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.repository.HomeRepository
import agstack.gramophone.ui.profileselection.ProfileSelectionNavigator
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ProfileSelectionViewModel @Inject constructor(
    @ApplicationContext context: Context

) : BaseViewModel<ProfileSelectionNavigator>() {



}