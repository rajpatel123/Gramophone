package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemFeatureProductBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PopularProductAdapter :
    RecyclerView.Adapter<PopularProductAdapter.CustomViewHolder>() {
    var onItemClicked: ((id: String) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemFeatureProductBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {
        holder.binding.productDetailsContainer.setOnClickListener {
            onItemClicked?.invoke("1")
        }
    }

    override fun getItemCount(): Int {
        return /*languageList.size*/4
    }

    inner class CustomViewHolder(var binding: ItemFeatureProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}