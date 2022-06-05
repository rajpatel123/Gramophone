package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemShopByCropsBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ShopByCropsAdapter :
    RecyclerView.Adapter<ShopByCropsAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemShopByCropsBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {

    }

    override fun getItemCount(): Int {
        return /*languageList.size*/9
    }

    inner class CustomViewHolder(var binding: ItemShopByCropsBinding) :
        RecyclerView.ViewHolder(binding.root)
}