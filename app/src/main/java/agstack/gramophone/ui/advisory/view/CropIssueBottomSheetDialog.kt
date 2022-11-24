package agstack.gramophone.ui.advisory.view

import agstack.gramophone.R
import agstack.gramophone.databinding.CropIssueBottomsheetDialogBinding
import agstack.gramophone.ui.advisory.AdvisoryActivity
import agstack.gramophone.ui.advisory.adapter.CropIssueImageListAdapter
import agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.CROP_IMAGE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNull
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_advisory.*
import kotlinx.android.synthetic.main.crop_issue_bottomsheet_dialog.*

class CropIssueBottomSheetDialog : BottomSheetDialogFragment() {
    private  var gpApiResponse: GpApiResponseData?=null
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
        if (gpApiResponse.isNotNull()){
            binding?.llCropIssueImageBottom?.visibility=VISIBLE
            binding?.llCropIssueDetails?.visibility= GONE
            binding?.issueDesc?.text = gpApiResponse?.stage_description
            rvCropImage.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            rvCropImage.setHasFixedSize(true)
            rvCropImage.adapter = gpApiResponse?.crop_stage_images?.let {
                CropIssueImageListAdapter(
                    it
                )
            }

        }else{
            binding?.llCropIssueImageBottom?.visibility=GONE
            binding?.llCropIssueDetails?.visibility= VISIBLE
            Glide.with(activity as AdvisoryActivity).load(bundle?.getString(CROP_IMAGE)).into(binding?.cropImage!!)
            binding?.txtDuration?.text = bundle?.getString(Constants.CROP_DURATION).plus(" - ".plus(bundle?.getString(Constants.CROP_END_DATE)))
            binding?.cropName?.text = bundle?.getString(Constants.CROP_NAME)
            binding?.txtStage?.text = "   ".plus(bundle?.getString(Constants.CROP_DAYS)).plus(" ".plus(getString(R.string.days)))
            binding?.txtAreaDesc?.text =" ".plus(getString(R.string.crop_duration_advisory)).plus(" ")
            binding?.stageName?.text = bundle?.getString(Constants.CROP_STAGE)
        }
    }

    fun setAcceptRejectListener(listener: AcceptRejectListener?) {
        this.listener = listener
    }

    fun setData(gpApiResponseData: GpApiResponseData) {
        gpApiResponse = gpApiResponseData
    }


    interface AcceptRejectListener {
        fun onAcceptRejectClick(friendRequestId: Int, status: Boolean)
    }
}