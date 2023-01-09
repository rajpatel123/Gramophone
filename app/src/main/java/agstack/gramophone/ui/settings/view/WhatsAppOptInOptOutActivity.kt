package agstack.gramophone.ui.settings.view

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityWhatsAppOptInOptOutBinding
import agstack.gramophone.ui.settings.WhatsappNavigator
import agstack.gramophone.ui.settings.viewmodel.WhatsAppOptINOutViewModel
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WhatsAppOptInOptOutActivity :
    BaseActivityWrapper<ActivityWhatsAppOptInOptOutBinding, WhatsappNavigator, WhatsAppOptINOutViewModel>(),
    WhatsappNavigator {

    private val whatsAppOptINOutViewModel: WhatsAppOptINOutViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(
            enableBackButton = true, getMessage(R.string.whatsapp),
            R.drawable.ic_arrow_left
        )
        whatsAppOptINOutViewModel.updateAction()
    }

    override fun getLayoutID(): Int = R.layout.activity_whats_app_opt_in_opt_out

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): WhatsAppOptINOutViewModel = whatsAppOptINOutViewModel

    override fun onSuccess(message: String?) {
        showToast(message)
        if (SharedPreferencesHelper.instance?.getBoolean(SharedPreferencesKeys.WHATSAPP_OPT_IN) == true) {
            sendOptInMoEngageEvent()
        }
        finish()
    }

    override fun onError(message: String?) {
        showToast(message)
    }

    private fun sendOptInMoEngageEvent() {
        val properties = Properties()
            .addAttribute("App Profile ID", SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("mobile number", SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.USER_MOBILE)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_WhatsApp_Opt_In", properties)
    }
}