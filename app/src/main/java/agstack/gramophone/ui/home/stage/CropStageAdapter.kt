package agstack.gramophone.ui.home.stage


import agstack.gramophone.databinding.ItemCategoryBinding
import agstack.gramophone.databinding.ItemCropStageBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Singleton

@Singleton
class CropStageAdapter :
    RecyclerView.Adapter<CropStageAdapter.CustomViewHolder>() {
    var onItemClicked: ((id: String) -> Unit)? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemCropStageBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.itemView.setOnClickListener {
            onItemClicked?.invoke("1")
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CustomViewHolder(var binding: ItemCropStageBinding) :
        RecyclerView.ViewHolder(binding.root)
}