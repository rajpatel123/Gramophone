package agstack.gramophone.ui.weather

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.weather.WeatherRepository
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : BaseViewModel<WeatherNavigator>() {

    var progress = MutableLiveData<Boolean>()
    var isRainView = MutableLiveData<Boolean>()
    var showWeatherView = MutableLiveData<Boolean>()

    init {
        progress.value = false
        isRainView.value = false
        showWeatherView.value = false
    }

    fun getWeatherData() {
        getNavigator()?.setHourWiseForecastAdapter(HourWiseForecastAdapter())

        getNavigator()?.setDayWiseForecastAdapter(DayWiseForecastAdapter())
    }

    fun weatherChange() {
        isRainView.value = isRainView.value != true
    }
}
