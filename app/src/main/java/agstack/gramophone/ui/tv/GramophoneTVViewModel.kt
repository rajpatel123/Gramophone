package agstack.gramophone.ui.tv

import agstack.gramophone.base.BaseViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class GramophoneTVViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : BaseViewModel<GramophoneTVNavigator>() {

    var progress = MutableLiveData<Boolean>()


    init {
        progress.value = false
    }
}
