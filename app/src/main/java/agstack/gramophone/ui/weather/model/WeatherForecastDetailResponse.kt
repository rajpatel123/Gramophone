package agstack.gramophone.ui.weather.model

data class WeatherForecastDetailResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: List<GpApiForecastDetailResponseData>,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

data class GpApiForecastDetailResponseData(
    val times: List<Time>
)

data class Time(
    val date: String,
    val temperature: Temperature,
    val time: String
)