package agstack.gramophone.ui.dialog

import agstack.gramophone.databinding.DialogLocationAccessBinding
import agstack.gramophone.ui.home.subcategory.model.Offer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

class LocationAccessDialog(private val listener: ((String) -> Unit)) : DialogFragment() {
    var binding: DialogLocationAccessBinding? = null

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
            listener.invoke("")
            dismiss()
        }
    }
}