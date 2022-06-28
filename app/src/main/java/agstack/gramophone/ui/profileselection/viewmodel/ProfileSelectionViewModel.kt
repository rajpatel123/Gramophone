package agstack.gramophone.ui.profileselection.viewmodel

import agstack.gramophone.ui.profileselection.model.UpdateProfileTypeRes
import agstack.gramophone.ui.profileselection.repository.ProfileSelectionRepository
import agstack.gramophone.utils.Resource
import agstack.gramophone.utils.hasInternetConnection
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.profileselection.ProfileSelectionNavigator
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileSelectionViewModel @Inject constructor(
    private val repository: ProfileSelectionRepository

) : BaseViewModel<ProfileSelectionNavigator>() { }
