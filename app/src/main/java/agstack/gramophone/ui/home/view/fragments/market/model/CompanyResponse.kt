package agstack.gramophone.ui.home.view.fragments.market.model

data class CompanyResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseCompanyData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)


data class GpApiResponseCompanyData(
    val companies_list: List<CompanyData>
)

data class CompanyData(
    val company_id: Int,
    val company_image: String,
    val company_name: String
)