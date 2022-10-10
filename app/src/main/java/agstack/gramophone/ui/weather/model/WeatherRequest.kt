package agstack.gramophone.ui.weather.model

data class WeatherRequest(
    var date: String? = null,
    var language: String? = null,
    var latitude: String? = null,
    var longitude: String? = null,
)
