package agstack.gramophone.ui.search.adapter

import agstack.gramophone.databinding.SubItemCategoryBinding
import agstack.gramophone.ui.search.model.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CropsCategoriesAdapter(
    val list: List<Item>,
    private val listener: (String, String?, String?) -> Unit,
) : RecyclerView.Adapter<CropsCategoriesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SubItemCategoryBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = list[position]
        holder.binding.item = category

        holder.binding.categoryContainer.setOnClickListener {
            listener.invoke(category.id!!, category.name, category.image)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: SubItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}