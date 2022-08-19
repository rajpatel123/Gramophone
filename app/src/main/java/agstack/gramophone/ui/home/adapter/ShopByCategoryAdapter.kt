package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemCategoryBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ShopByCategoryAdapter :
    RecyclerView.Adapter<ShopByCategoryAdapter.DeveloperViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        return DeveloperViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, i: Int) {
        holder.binding.tvCatName.text = "Onion"

    }

    override fun getItemCount(): Int {
        return /*languageList.size*/10
    }

    inner class DeveloperViewHolder(var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}