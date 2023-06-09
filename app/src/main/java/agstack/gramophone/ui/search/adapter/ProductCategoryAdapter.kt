package agstack.gramophone.ui.search.adapter

import agstack.gramophone.databinding.SubItemCategoryBinding
import agstack.gramophone.ui.search.model.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNullOrEmpty

class ProductCategoryAdapter(
    val list: List<Item>,
    private val listener: (String, String, String?, String?) -> Unit,
) : RecyclerView.Adapter<ProductCategoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SubItemCategoryBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = list[position]
        holder.binding.item = category

        holder.binding.categoryContainer.setOnClickListener {
            val parentCategoryId =
                if (category.category_parent_id.isNotNullOrEmpty()) category.category_parent_id!! else category.id!!
            listener.invoke(
                parentCategoryId,
                category.id!!,
                category.name,
                category.image
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: SubItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}