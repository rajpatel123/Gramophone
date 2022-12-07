package agstack.gramophone.ui.orderdetails.model

data class OrderInvoiceResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: InvoiceData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

data class InvoiceData(
    val invoice_url: String
)