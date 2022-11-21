package agstack.gramophone.ui.advisory.view

import agstack.gramophone.R
import agstack.gramophone.databinding.CropIssueBottomsheetDialogBinding
import agstack.gramophone.ui.advisory.AdvisoryActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.CROP_IMAGE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CropIssueBottomSheetDialog : BottomSheetDialogFragment() {
    var binding: CropIssueBottomsheetDialogBinding? = null
    var listener: AcceptRejectListener? = null
    var bundle: Bundle? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CropIssueBottomsheetDialogBinding.inflate(inflater)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(activity as AdvisoryActivity).load(bundle?.getString(CROP_IMAGE)).into(binding?.cropImage!!)
        binding?.txtDuration?.text = bundle?.getString(Constants.CROP_DURATION)
        binding?.cropName?.text = bundle?.getString(Constants.CROP_NAME)
        binding?.txtStage?.text = bundle?.getString(Constants.CROP_DAYS)
        binding?.txtAreaDesc?.text =getString(R.string.crop_duration_advisory).plus(" ")
        binding?.stageName?.text = bundle?.getString(Constants.CROP_STAGE)
    }

    fun setAcceptRejectListener(listener: AcceptRejectListener?) {
        this.listener = listener
    }


    interface AcceptRejectListener {
        fun onAcceptRejectClick(friendRequestId: Int, status: Boolean)
    }
}