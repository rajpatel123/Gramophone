package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemRecentlyViewedBinding
import agstack.gramophone.ui.cart.model.CartItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecentlyViewedAdapter(
    cartItemList: List<CartItem>?,
    private val listener: (String) -> Unit,
) :
    RecyclerView.Adapter<RecentlyViewedAdapter.CustomViewHolder>() {
    var cartList = cartItemList
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemRecentlyViewedBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.model = cartList?.get(position)
        holder.binding.productDetailsContainer.setOnClickListener {
            listener.invoke(cartList?.get(position)?.product_id.toString())
        }
    }

    override fun getItemCount(): Int {
        return cartList?.size!!
    }

    inner class CustomViewHolder(var binding: ItemRecentlyViewedBinding) :
        RecyclerView.ViewHolder(binding.root)
}