package agstack.gramophone.ui.cart.adapter


import agstack.gramophone.databinding.ItemCartBinding
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.cart.model.OfferApplied
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Singleton

@Singleton
class CartAdapter(cartItemList: List<CartItem>) :
    RecyclerView.Adapter<CartAdapter.CustomViewHolder>() {
    var cartList = cartItemList
    var onItemDetailClicked: ((productId: String) -> Unit)? = null
    var onItemDeleteClicked: ((productId: String) -> Unit)? = null
    var onOfferClicked: ((offerAppliedList: List<OfferApplied>) -> Unit)? = null
    var onQuantityClicked: ((cartItem: CartItem) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemCartBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.model = cartList[position]
        var quantity = cartList[position].quantity.toInt()
        holder.binding.ivProduct.setOnClickListener {
            onItemDetailClicked?.invoke(cartList[position].product_id)
        }
        holder.binding.ivDelete.setOnClickListener {
            onItemDeleteClicked?.invoke(cartList[position].product_id)
        }
        holder.binding.tvOffersApplied.setOnClickListener {
            onOfferClicked?.invoke(cartList[position].offer_applied)
        }
        holder.binding.ivSubtract.setOnClickListener {
            if (quantity > 1) {
                quantity -= 1
                cartList[position].quantity = quantity.toString()
                notifyDataSetChanged()
            }
            onQuantityClicked?.invoke(cartList[position])
        }
        holder.binding.ivAdd.setOnClickListener {
            quantity += 1
            cartList[position].quantity = quantity.toString()
            notifyDataSetChanged()
            onQuantityClicked?.invoke(cartList[position])
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CustomViewHolder(var binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)
}