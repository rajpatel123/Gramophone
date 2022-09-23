package agstack.gramophone.ui.dialog

import agstack.gramophone.databinding.DialogLocationAccessBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

class LocationAccessDialog : DialogFragment() {
    var binding: DialogLocationAccessBinding? = null
    var listener: OkCancelListener? = null

    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): LocationAccessDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = LocationAccessDialog()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogLocationAccessBinding.inflate(inflater)
        setupUi()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupUi() {
        binding?.tvNoThanks?.setOnClickListener {
            dismiss()
        }

        binding?.tvOk?.setOnClickListener {
            listener?.onGoToSettingClick()
            dismiss()
        }
    }

    interface OkCancelListener {
        fun onGoToSettingClick()
    }
}