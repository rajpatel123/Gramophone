package agstack.gramophone.ui.home.viewmodel

import agstack.gramophone.ui.home.repository.HomeRepository

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class MarketFragmentViewModel @Inject constructor(
    private val homeRepository: HomeRepository

) : ViewModel() {


}