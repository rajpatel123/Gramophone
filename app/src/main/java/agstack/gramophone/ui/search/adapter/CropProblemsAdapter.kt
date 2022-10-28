package agstack.gramophone.ui.search.adapter

import agstack.gramophone.databinding.SubItemCropBinding
import agstack.gramophone.databinding.SubItemCropProblemsBinding
import agstack.gramophone.ui.search.model.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CropProblemsAdapter (
    val list: List<Item>,
    private val listener: (String) -> Unit,
) : RecyclerView.Adapter<CropProblemsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SubItemCropProblemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = list[position]
        holder.binding.item = category

        holder.binding.itemView.setOnClickListener {
            listener.invoke(category.id!!)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: SubItemCropProblemsBinding) :
        RecyclerView.ViewHolder(binding.root)
}