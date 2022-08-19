package agstack.gramophone.data.repository.weather

import agstack.gramophone.di.GramAppService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val gramAppService: GramAppService,
) : WeatherRepository {

}