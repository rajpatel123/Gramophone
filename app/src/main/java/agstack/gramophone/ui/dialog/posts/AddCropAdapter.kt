package agstack.gramophone.ui.dialog.posts

import agstack.gramophone.databinding.ItemAddCropBinding
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AddCropAdapter(
    val cropList: MutableList<CropData>?,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val viewTypeItem = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SelectCropItemViewHolder(
            ItemAddCropBinding.inflate(LayoutInflater.from(parent.context))
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as SelectCropItemViewHolder
        h.binding.model = cropList?.get(position)
        h.binding.itemView.setOnClickListener {
            cropList?.get(position)?.isSelected = !cropList?.get(position)?.isSelected!!
            notifyItemChanged(position)
        }

    }


    override fun getItemViewType(position: Int): Int {
        return viewTypeItem
    }

    override fun getItemCount(): Int {
        return cropList?.size!!
    }

    inner class SelectCropItemViewHolder(var binding: ItemAddCropBinding) :
        RecyclerView.ViewHolder(binding.root)

}