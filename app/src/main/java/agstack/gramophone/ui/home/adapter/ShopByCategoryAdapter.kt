package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemCategoryBinding
import agstack.gramophone.ui.home.view.fragments.market.model.CategoryData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ShopByCategoryAdapter(
    private var categoryList: List<CategoryData>?,
    private val listener: (String) -> Unit,
) :
    RecyclerView.Adapter<ShopByCategoryAdapter.DeveloperViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        return DeveloperViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, i: Int) {
        holder.binding.model = categoryList?.get(i)
        holder.itemView.setOnClickListener {
            listener.invoke(categoryList?.get(i)?.category_id?.toString()!!)
        }
    }

    override fun getItemCount(): Int {
        return categoryList?.size!!
    }

    inner class DeveloperViewHolder(var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}