package agstack.gramophone.ui.weather

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.weather.WeatherRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.weather.model.GpApiWeatherResponseData
import agstack.gramophone.ui.weather.model.WeatherRequest
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Utility
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.bumptech.glide.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : BaseViewModel<WeatherNavigator>() {

    var progress = MutableLiveData<Boolean>()
    var isRainView = MutableLiveData<Boolean>()
    var showWeatherView = MutableLiveData<Boolean>()
    var address = MutableLiveData<String>()
    var currentTime = MutableLiveData<String>()
    var currentTemp = MutableLiveData<String>()
    var minTemp = MutableLiveData<String>()
    var maxTemp = MutableLiveData<String>()
    var weatherCondition = MutableLiveData<String>()
    var perceptionIntensity = MutableLiveData<String>()
    var perceptionType = MutableLiveData<String>()

    init {
        progress.value = false
        isRainView.value = false
        showWeatherView.value = false
    }

    fun getWeatherData() {
        getNavigator()?.setToolbarTitle(getNavigator()?.getMessage(R.string.weather)!!)
    }

    fun weatherChange() {
        isRainView.value = isRainView.value != true
    }

    fun getWeatherDetail() {
        val weatherRequest = WeatherRequest(null, null, "28.7041", "77.1025")
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response = weatherRepository.getWeatherDetail(weatherRequest)
                    //stop loader
                    progress.value = false
                    if (response.body()?.gp_api_status.equals(Constants.GP_API_STATUS)
                        && response.body()?.gp_api_response_data.isNotNullOrEmpty()
                        && response.body()?.gp_api_response_data?.get(0).isNotNull()
                    ) {
                        val weatherData = response.body()?.gp_api_response_data?.get(0)
                        getNavigator()?.setToolbarTitle(getNavigator()?.getMessage(R.string.weather)!! + " - " + weatherData?.city!!)
                        address.value = weatherData?.address
                        currentTemp.value = weatherData?.temperature?.current
                        minTemp.value = weatherData?.temperature?.min
                        maxTemp.value = weatherData?.temperature?.max
                        weatherCondition.value = weatherData?.temperature?.weather_condition
                        perceptionIntensity.value = weatherData?.temperature?.perception_intensity
                        perceptionType.value = weatherData?.temperature?.perception_type

                        try {
                            var date: String = weatherData?.current_time!!
                            date = date.replace("Today - ", "")
                            val time = Utility.getFormattedDate(date,
                                Utility.HOUR_MIN_12_TIME_FORMAT,
                                Utility.HOUR_MIN_SECOND_TIME_FORMAT)
                            currentTime.value = "Today - $time"
                        } catch (e: Exception) {
                            currentTime.value = weatherData?.current_time
                        }
                    }
                }
            } catch (e: Exception) {
                progress.value = false
            }
        }
    }

    fun getWeatherDetailHourly() {
        val weatherRequest = WeatherRequest("2022-10-10", null, "28.7041", "77.1025")
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response = weatherRepository.getWeatherForecastDetailHourly(weatherRequest)
                    //stop loader
                    progress.value = false
                    if (response.body()?.gp_api_status.equals(Constants.GP_API_STATUS)
                        && response.body()?.gp_api_response_data.isNotNullOrEmpty()
                        && response.body()?.gp_api_response_data?.get(0).isNotNull()
                        && response.body()?.gp_api_response_data?.get(0)?.times.isNotNullOrEmpty()
                    ) {

                        getNavigator()?.setHourWiseForecastAdapter(HourWiseForecastAdapter(response.body()?.gp_api_response_data?.get(
                            0)!!.times))
                    }
                }
            } catch (e: Exception) {
                progress.value = false
            }
        }
    }

    fun getWeatherDetailDayWise() {
        val weatherRequest = WeatherRequest(null, null, "28.7041", "77.1025")
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response =
                        weatherRepository.getWeatherForecastSummaryDayWise(weatherRequest)
                    //stop loader
                    progress.value = false
                    if (response.body()?.gp_api_status.equals(Constants.GP_API_STATUS)
                        && response.body()?.gp_api_response_data.isNotNullOrEmpty()
                        && response.body()?.gp_api_response_data?.get(0).isNotNull()
                        && response.body()?.gp_api_response_data?.get(0)?.days.isNotNullOrEmpty()
                    ) {

                        getNavigator()?.setDayWiseForecastAdapter(DayWiseForecastAdapter(response.body()?.gp_api_response_data?.get(
                            0)!!.days))
                    }
                }
            } catch (e: Exception) {
                progress.value = false
            }
        }
    }
}
