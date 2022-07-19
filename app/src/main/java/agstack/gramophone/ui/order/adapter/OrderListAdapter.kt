package agstack.gramophone.ui.order.adapter


import agstack.gramophone.databinding.ItemCartBinding
import agstack.gramophone.databinding.ItemOrderBinding
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OrderListAdapter :
    RecyclerView.Adapter<OrderListAdapter.CustomViewHolder>() {
    var selectedProduct: (() -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemOrderBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {
        holder.binding.itemOrder.setOnClickListener {

            selectedProduct?.invoke()
        }
    }

    override fun getItemCount(): Int {
        return /*languageList.size*/6
    }

    inner class CustomViewHolder(var binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)
}