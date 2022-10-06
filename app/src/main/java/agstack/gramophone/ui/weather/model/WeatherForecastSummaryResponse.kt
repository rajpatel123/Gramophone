package agstack.gramophone.ui.weather.model

data class WeatherForecastSummaryResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: List<GpApiForecastSummaryResponseData>,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

data class GpApiForecastSummaryResponseData(
    val days: List<Day>
)

data class Day(
    val date: String,
    val day: String,
    val format_date: String,
    val temperature: Temperature
)