package agstack.gramophone.ui.search.adapter

import agstack.gramophone.R
import agstack.gramophone.databinding.SubItemCropProblemsBinding
import agstack.gramophone.ui.search.model.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView

class CropProblemsAdapter(
    val list: List<Item>,
    private val listener: (String, String?, String?) -> Unit,
) : RecyclerView.Adapter<CropProblemsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SubItemCropProblemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val crop = list[position]
        holder.binding.item = crop

        holder.binding.itemView.setOnClickListener {
            listener.invoke(crop.id!!, crop.name, crop.image)
        }
        if (position % 2 == 0) {
            holder.binding.bottomHalf.background = AppCompatResources.getDrawable(
                holder.binding.bottomHalf.context,
                R.drawable.rounded_corner_bottom_16_solid_yellow_stroke_gray
            )
        } else {
            holder.binding.bottomHalf.background = AppCompatResources.getDrawable(
                holder.binding.bottomHalf.context,
                R.drawable.rounded_corner_bottom_16_solid_pink_stroke_gray
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: SubItemCropProblemsBinding) :
        RecyclerView.ViewHolder(binding.root)
}