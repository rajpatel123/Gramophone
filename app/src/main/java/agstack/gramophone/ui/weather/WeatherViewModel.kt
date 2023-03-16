package agstack.gramophone.ui.weather

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.weather.WeatherRepository
import agstack.gramophone.di.GPSTracker
import agstack.gramophone.ui.weather.model.WeatherRequest
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.Utility
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : BaseViewModel<WeatherNavigator>() {

    var progress = MutableLiveData<Boolean>()
    var isRainView = MutableLiveData<Boolean>()
    var showWeatherView = MutableLiveData<Boolean>()
    var weatherTypeImage = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var currentTime = MutableLiveData<String>()
    var currentTemp = MutableLiveData<String>()
    var minTemp = MutableLiveData<String>()
    var maxTemp = MutableLiveData<String>()
    var weatherCondition = MutableLiveData<String>()
    var perceptionIntensity = MutableLiveData<String>()
    var perceptionType = MutableLiveData<String>()
    var latitude: String? = null
    var longitude: String? = null

    init {
        progress.value = false
        isRainView.value = false
        showWeatherView.value = false
        minTemp.value = ""
        perceptionIntensity.value = ""
        perceptionType.value = ""
        latitude = null
        longitude = null
    }

    fun getWeatherData() {
        getWeatherDetail()
        getWeatherDetailHourly()
        getWeatherDetailDayWise()
    }

    fun weatherChange() {
        isRainView.value = isRainView.value != true
    }

    private fun getWeatherDetail() {
        val weatherRequest = WeatherRequest(SharedPreferencesHelper.instance?.getString(Constants.LATITUDE), SharedPreferencesHelper.instance?.getString(Constants.LONGITUDE))
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
                        weatherTypeImage.value = weatherData?.temperature?.weather_icon
                        minTemp.value =
                            if (weatherData?.temperature?.min == null || weatherData.temperature.min.isEmpty()) "" else weatherData.temperature.min
                        maxTemp.value = weatherData?.temperature?.max
                        weatherCondition.value = weatherData?.temperature?.weather_condition
                        perceptionIntensity.value =
                            if (weatherData?.temperature?.perception_intensity == null || weatherData.temperature.perception_intensity.isEmpty()) "" else weatherData.temperature.perception_intensity
                        perceptionType.value =
                            if (weatherData?.temperature?.perception_type == null || weatherData.temperature.perception_type.isEmpty()) "" else weatherData.temperature.perception_type
                        currentTime.value = weatherData?.current_time
                    }
                }
            } catch (e: Exception) {
                progress.value = false
            }
        }
    }

    private fun getWeatherDetailHourly() {
        val weatherRequest = WeatherRequest(latitude, longitude)
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

    private fun getWeatherDetailDayWise() {
        val weatherRequest = WeatherRequest(latitude, longitude)
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

    fun getLatitudeLongitude() {
        try {
            val gpsTracker = getNavigator()?.getGPSTracker()
            if (gpsTracker != null && gpsTracker.canGetLocation()) {
                latitude = gpsTracker.getLatitude().toString()
                longitude = gpsTracker.getLongitude().toString()
                // refresh weather data
                if (latitude.equals("0.0")) {
                    latitude = null
                    longitude = null
                }else{
                    SharedPreferencesHelper.instance?.putString(Constants.LATITUDE,"".plus(latitude))
                    SharedPreferencesHelper.instance?.putString(Constants.LONGITUDE,"".plus(longitude))
                }
                getWeatherData()
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gpsTracker!!.showSettingsAlert()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
