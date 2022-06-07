package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemShopByStoreBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ShopByStoresAdapter :
    RecyclerView.Adapter<ShopByStoresAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemShopByStoreBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {

    }

    override fun getItemCount(): Int {
        return /*languageList.size*/4
    }

    inner class CustomViewHolder(var binding: ItemShopByStoreBinding) :
        RecyclerView.ViewHolder(binding.root)
}