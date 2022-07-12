package agstack.gramophone.ui.language.model

data class GpApiResponseData(
    val current_version_code: Int,
    val external_link_list: ExternalLinkList,
    val help_data_list: HelpDataList,
    val is_force_update_app: String,
    val is_latest_version: String,
    val login_banner_list: List<LoginBanner>,
    val temp_token: String,
    val update_message: String
)