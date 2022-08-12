package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemProductListBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Singleton

@Singleton
class ProductListAdapter() :
    RecyclerView.Adapter<ProductListAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemProductListBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

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

    inner class CustomViewHolder(var binding: ItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root)
}