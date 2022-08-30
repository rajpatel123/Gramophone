package agstack.gramophone.ui.home.subcategory.adapter


import agstack.gramophone.databinding.ItemCategoryBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Singleton

@Singleton
class SubCategoryAdapter() :
    RecyclerView.Adapter<SubCategoryAdapter.CustomViewHolder>() {
    var onItemClicked: ((productId: String) -> Unit)? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.itemView.setOnClickListener {
            onItemClicked?.invoke("1")
        }
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CustomViewHolder(var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}