package agstack.gramophone.ui.settings.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityWhatsAppOptInOptOutBinding
import agstack.gramophone.ui.settings.WhatsappNavigator
import agstack.gramophone.ui.settings.viewmodel.WhatsAppOptINOutViewModel
import android.os.Bundle
import androidx.activity.viewModels
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
    }

    override fun getLayoutID(): Int = R.layout.activity_whats_app_opt_in_opt_out

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): WhatsAppOptINOutViewModel = whatsAppOptINOutViewModel

    override fun onSuccess(message: String?) {
        showToast(message)
        finish()
    }

    override fun onError(message: String?) {
        showToast(message)
    }
}