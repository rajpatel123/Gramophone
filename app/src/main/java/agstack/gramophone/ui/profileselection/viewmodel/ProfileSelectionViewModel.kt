package agstack.gramophone.ui.profileselection.viewmodel

import agstack.gramophone.ui.profileselection.repository.ProfileSelectionRepository
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.profileselection.ProfileSelectionNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileSelectionViewModel @Inject constructor(
    private val repository: ProfileSelectionRepository

) : BaseViewModel<ProfileSelectionNavigator>() { }
