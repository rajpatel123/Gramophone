package agstack.gramophone.ui.farm.adapter

import agstack.gramophone.databinding.ItemSectionSelectCropBinding
import agstack.gramophone.databinding.ItemSelectCropBinding
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SelectCropAdapter(
    private val cropList: List<CropData>?,
    private val listener: (CropData?) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
     val viewTypeHeader = 0
     private val viewTypeItem = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): RecyclerView.ViewHolder {
        return if (viewType == viewTypeItem) {
            SelectCropItemViewHolder(
                ItemSelectCropBinding.inflate(LayoutInflater.from(parent.context)))
        } else  {
            SectionHeaderViewHolder(ItemSectionSelectCropBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            viewTypeItem -> {
                val h = holder as SelectCropItemViewHolder
                h.binding.model = cropList?.get(position)
                h.binding.itemView.setOnClickListener {
                    listener.invoke(cropList?.get(position))
                }
            }
            viewTypeHeader -> {
                val h = holder as SectionHeaderViewHolder
                h.binding.model = cropList?.get(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (cropList?.get(position)?.isSectionHeader!!) {
            viewTypeHeader
        } else {
            viewTypeItem
        }
    }

    override fun getItemCount(): Int {
        return cropList?.size!!
    }

    inner class SelectCropItemViewHolder(var binding: ItemSelectCropBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class SectionHeaderViewHolder(var binding: ItemSectionSelectCropBinding) :
        RecyclerView.ViewHolder(binding.root)
}