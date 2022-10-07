package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemShopByStoreBinding
import agstack.gramophone.ui.home.view.fragments.market.model.StoreData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ShopByStoresAdapter(private val storeList: List<StoreData>?,
                          private val listener: (String, String, String) -> Unit) :
    RecyclerView.Adapter<ShopByStoresAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemShopByStoreBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {
        holder.binding.model = storeList!![i]
        holder.binding.itemView.setOnClickListener {
            listener.invoke(storeList[i].storeId.toString(), storeList[i].storeName!!, storeList[i].storeImage!!)
        }
    }

    override fun getItemCount(): Int {
        return storeList?.size!!
    }

    inner class CustomViewHolder(var binding: ItemShopByStoreBinding) :
        RecyclerView.ViewHolder(binding.root)
}