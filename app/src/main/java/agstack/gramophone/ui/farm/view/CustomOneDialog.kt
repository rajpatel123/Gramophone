package agstack.gramophone.ui.farm.view

import agstack.gramophone.databinding.InfoSubmittedDialogBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment

class CustomOneDialog (private val listener: () -> Unit) : DialogFragment() {

    var binding : InfoSubmittedDialogBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InfoSubmittedDialogBinding.inflate(layoutInflater)

        binding?.notNow?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.addFarmText?.setOnClickListener {
            listener.invoke()
            dialog?.dismiss()
        }

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE);
        }

        return binding?.root!!
    }

}