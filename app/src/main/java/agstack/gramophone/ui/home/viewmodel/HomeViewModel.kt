package agstack.gramophone.ui.home.viewmodel

import agstack.gramophone.ui.home.repository.HomeRepository
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    @ApplicationContext context: Context

) : ViewModel() {



}