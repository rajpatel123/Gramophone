package agstack.gramophone.data.repository.weather

import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.home.subcategory.model.SubCategoryResponse
import agstack.gramophone.ui.weather.model.WeatherForecastDetailResponse
import agstack.gramophone.ui.weather.model.WeatherForecastSummaryResponse
import agstack.gramophone.ui.weather.model.WeatherRequest
import agstack.gramophone.ui.weather.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val gramAppService: GramAppService,
) : WeatherRepository {

    override suspend fun getWeatherDetail(weatherRequest: WeatherRequest): Response<WeatherResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramAppService.getWeatherDetails(weatherRequest)
            response
        }

    override suspend fun getWeatherForecastDetailHourly(weatherRequest: WeatherRequest): Response<WeatherForecastDetailResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramAppService.getWeatherForeCastDetailHourly(weatherRequest)
            response
        }

    override suspend fun getWeatherForecastSummaryDayWise(weatherRequest: WeatherRequest): Response<WeatherForecastSummaryResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramAppService.getWeatherForecastSummaryDayWise(weatherRequest)
            response
        }
}