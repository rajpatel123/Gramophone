package agstack.gramophone.ui.home.view.fragments.community.model.quiz

data class QuizPollResponseModel(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: List<List<GpApiResponseData>>,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)