package agstack.gramophone.ui.weather

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.di.GPSTracker

interface WeatherNavigator : BaseNavigator {
    fun setToolbarTitle(title:String)

    fun setHourWiseForecastAdapter(
        hourWiseForecastAdapter: HourWiseForecastAdapter,
    )

    fun setDayWiseForecastAdapter(
        dayWiseForecastAdapter: DayWiseForecastAdapter,
    )
}
