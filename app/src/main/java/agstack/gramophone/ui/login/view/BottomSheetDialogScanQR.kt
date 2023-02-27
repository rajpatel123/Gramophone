package agstack.gramophone.ui.login.view

import agstack.gramophone.databinding.ScanQrSheetBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetDialogScanQR : BottomSheetDialogFragment() {
    lateinit var customerSupportNumber: String
    lateinit var sourceScreen: String
    var binding: ScanQrSheetBinding? = null
    var listener: OnClickEvents? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScanQrSheetBinding.inflate(inflater)

        setupUi()
        return binding!!.root
    }

    private fun setupUi() {
        binding?.ivCloseDialog?.setOnClickListener {
            dismiss()
        }

        binding?.cameraIv?.setOnClickListener {
            listener?.onCameraCLick()
            dismiss()
        }

        binding?.galleryIv?.setOnClickListener {
            listener?.onGallaryClick()
            dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun setOnClickEventsListener(listener: OnClickEvents?) {
        this.listener = listener
    }

    interface OnClickEvents {
        fun onCameraCLick()
        fun onGallaryClick()
    }
}
