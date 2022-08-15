package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemShopByCompanyBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ShopByCompanyAdapter :
    RecyclerView.Adapter<ShopByCompanyAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemShopByCompanyBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {

    }

    override fun getItemCount(): Int {
        return /*languageList.size*/6
    }

    inner class CustomViewHolder(var binding: ItemShopByCompanyBinding) :
        RecyclerView.ViewHolder(binding.root)
}