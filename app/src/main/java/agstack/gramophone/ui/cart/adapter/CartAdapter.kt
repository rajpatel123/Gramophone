package agstack.gramophone.ui.cart.adapter


import agstack.gramophone.databinding.ItemCartBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CartAdapter :
    RecyclerView.Adapter<CartAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemCartBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {

    }

    override fun getItemCount(): Int {
        return /*languageList.size*/3
    }

    inner class CustomViewHolder(var binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)
}