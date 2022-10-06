package agstack.gramophone.ui.weather.model

data class WeatherResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: List<GpApiWeatherResponseData>,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

class GpApiErrorData

data class GpApiWeatherResponseData(
    val address: String,
    val city: String,
    val current_time: String,
    val temperature: Temperature
)

data class GpApiTrace(
    val gp_request_id: String,
    val gp_trace_id: String
)

data class Temperature(
    val current: String,
    val max: String,
    val min: String,
    val perception_intensity: String,
    val perception_type: String,
    val unit: String,
    val value: String,
    val weather_condition: String,
    val weather_icon: String
)