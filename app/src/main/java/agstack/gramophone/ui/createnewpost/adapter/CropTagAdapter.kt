package agstack.gramophone.ui.createnewpost.adapter

import agstack.gramophone.R
import agstack.gramophone.databinding.ItemCropOnBottomSheetBinding
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CropTagAdapter(
    var cropList: MutableList<CropData>?,
    var onOtherClick: OnOtherClick
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val viewTypeItem = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SelectCropItemViewHolder(
            ItemCropOnBottomSheetBinding.inflate(LayoutInflater.from(parent.context))
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as SelectCropItemViewHolder
        h.binding.finalTagText.text = cropList?.get(position)?.cropName

        if (cropList?.get(position)?.isSelected == true) {
            h.binding.llCrops.setBackgroundResource(R.drawable.tags_crop_selected)
        } else {
            h.binding.llCrops.setBackgroundResource(R.drawable.tags_crop_normal)

        }

        h.binding.finalTagText.setOnClickListener {
            cropList?.get(position)?.isSelected = cropList?.get(position)?.isSelected != true
            notifyDataSetChanged()
        }


        if (position == (cropList?.size!! -1)) {
            h.binding.otherText.visibility = VISIBLE
            h.binding.otherText.setOnClickListener {
             onOtherClick.onOtherClicked()
            }
        } else {
            h.binding.otherText.visibility = GONE
        }
    }


    override fun getItemViewType(position: Int): Int {
        return viewTypeItem
    }

    override fun getItemCount(): Int {

        return cropList?.size!!
    }

    fun setData(cropListOnSheet: ArrayList<CropData>) {
        this.cropList = cropListOnSheet
    }

    inner class SelectCropItemViewHolder(var binding: ItemCropOnBottomSheetBinding) :
        RecyclerView.ViewHolder(binding.root)


    interface OnOtherClick {
        fun onOtherClicked()
    }

}