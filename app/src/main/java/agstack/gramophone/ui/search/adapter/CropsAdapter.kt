package agstack.gramophone.ui.search.adapter

import agstack.gramophone.databinding.SubItemCropBinding
import agstack.gramophone.ui.search.model.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CropsAdapter(
    val list: List<Item>,
    private val listener: (String) -> Unit,
) : RecyclerView.Adapter<CropsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SubItemCropBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = list[position]
        holder.binding.item = category

        holder.binding.cropContainer.setOnClickListener {
            listener.invoke(category.id!!)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: SubItemCropBinding) :
        RecyclerView.ViewHolder(binding.root)
}