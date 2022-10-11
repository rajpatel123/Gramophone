package agstack.gramophone.data.repository.weather

import agstack.gramophone.ui.weather.model.WeatherForecastDetailResponse
import agstack.gramophone.ui.weather.model.WeatherForecastSummaryResponse
import agstack.gramophone.ui.weather.model.WeatherRequest
import agstack.gramophone.ui.weather.model.WeatherResponse
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface WeatherRepository {

    suspend fun getWeatherDetail(weatherRequest: WeatherRequest): Response<WeatherResponse>

    suspend fun getWeatherForecastDetailHourly(weatherRequest: WeatherRequest): Response<WeatherForecastDetailResponse>

    suspend fun getWeatherForecastSummaryDayWise(weatherRequest: WeatherRequest): Response<WeatherForecastSummaryResponse>
}