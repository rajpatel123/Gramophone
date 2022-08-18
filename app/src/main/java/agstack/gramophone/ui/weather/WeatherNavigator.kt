package agstack.gramophone.ui.weather

import agstack.gramophone.base.BaseNavigator

interface WeatherNavigator : BaseNavigator {
    fun setHourWiseForecastAdapter(
        hourWiseForecastAdapter: HourWiseForecastAdapter,
    )

    fun setDayWiseForecastAdapter(
        dayWiseForecastAdapter: DayWiseForecastAdapter,
    )
}
