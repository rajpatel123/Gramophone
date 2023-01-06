package agstack.gramophone.ui.dialog

import agstack.gramophone.BuildConfig
import agstack.gramophone.databinding.BottomSheetDialogHelpBinding
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.utils.Utility
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper


class BottomSheetDialog : BottomSheetDialogFragment() {
    lateinit var customerSupportNumber: String
    lateinit var sourceScreen: String
    var binding: BottomSheetDialogHelpBinding? = null
    var listener: AcceptRejectListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDialogHelpBinding.inflate(inflater)

        setupUi()
        return binding!!.root
    }

    private fun setupUi() {
        binding?.ivCloseDialog?.setOnClickListener {
            dismiss()
        }

        binding?.callMeNow?.setOnClickListener {
            sendMoEngageEvents()
            val intent = Intent(Intent.ACTION_CALL);
            intent.data = Uri.parse("tel:$customerSupportNumber")
            startActivity(intent)
            dismiss()
        }
    }

    private fun sendMoEngageEvents() {
        try {
            val properties = Properties()
            properties.addAttribute("Source_Screen", sourceScreen)
                .addAttribute("App Version", BuildConfig.VERSION_NAME)
                .addAttribute("SDK Version", Build.VERSION.SDK_INT)
                .setNonInteractive()
            MoEAnalyticsHelper.trackEvent(requireContext(), "KA_Help_Call_Now", properties)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun setAcceptRejectListener(listener: AcceptRejectListener?) {
        this.listener = listener
    }

    interface AcceptRejectListener {
        fun onAcceptRejectClick(friendRequestId: Int, status: Boolean)
    }
}