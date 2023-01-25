package agstack.gramophone.ui.dialog.posts

import agstack.gramophone.databinding.CropBottomSheetBinding
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.ui.home.view.fragments.market.model.CropResponse
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetCropsDialog(
    private val cropResponse: CropResponse,
    private val listener: OnSelectionDone
) : BottomSheetDialogFragment() {
    private lateinit var addCropAdapter: AddCropAdapter
    var binding: CropBottomSheetBinding? = null
    val cropListData =ArrayList<CropData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = CropBottomSheetBinding.inflate(inflater)

        setupUi()
        return binding!!.root
    }

    private fun setupUi() {

     addCropAdapter = AddCropAdapter(cropList = cropResponse.gpApiResponseData?.cropsList)
        val layoutManager = GridLayoutManager(activity, 3)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        binding?.rvCrops?.layoutManager = layoutManager
        binding?.rvCrops?.setHasFixedSize(true)
        binding?.rvCrops?.adapter = addCropAdapter


        binding?.doneButton?.setOnClickListener {
            addCropAdapter.cropList?.forEach {
                if (it.isSelected) cropListData.add(it)
            }
            listener.onCropSelectionDone(cropListData)
            dismiss()
        }
    }

    interface OnSelectionDone{
        fun onCropSelectionDone(cropList:MutableList<CropData>)
    }

}



