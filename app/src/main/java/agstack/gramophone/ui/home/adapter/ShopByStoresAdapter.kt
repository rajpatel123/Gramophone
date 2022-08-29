package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemShopByStoreBinding
import agstack.gramophone.ui.home.view.fragments.market.model.StoreData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ShopByStoresAdapter(private val storeList: List<StoreData>?) :
    RecyclerView.Adapter<ShopByStoresAdapter.CustomViewHolder>() {
    var onItemClicked: ((id: String) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemShopByStoreBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {
        holder.binding.model = storeList!![i]
        holder.binding.itemView.setOnClickListener {
            onItemClicked?.invoke(storeList[i].store_id.toString())
        }
    }

    override fun getItemCount(): Int {
        return storeList?.size!!
    }

    inner class CustomViewHolder(var binding: ItemShopByStoreBinding) :
        RecyclerView.ViewHolder(binding.root)
}