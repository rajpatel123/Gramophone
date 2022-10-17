package agstack.gramophone.ui.createnewpost.view

import agstack.gramophone.databinding.FinalTagListItemBinding
import agstack.gramophone.databinding.ItemAddCropBinding
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TagsCropAdapter(
    val cropList: MutableList<CropData>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val viewTypeItem = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SelectCropItemViewHolder(
            FinalTagListItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as SelectCropItemViewHolder
        h.binding.finalTagText.text= cropList?.get(position)?.cropName
        h.binding.removeTag.setOnClickListener {
            cropList?.get(position)?.isSelected = !cropList?.get(position)?.isSelected!!
            cropList.remove(cropList?.get(position))
            notifyDataSetChanged()
        }


    }


    override fun getItemViewType(position: Int): Int {
        return viewTypeItem
    }

    override fun getItemCount(): Int {
        return cropList?.size!!
    }

    inner class SelectCropItemViewHolder(var binding: FinalTagListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}