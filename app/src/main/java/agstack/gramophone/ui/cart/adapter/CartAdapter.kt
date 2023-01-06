package agstack.gramophone.ui.cart.adapter


import agstack.gramophone.databinding.ItemCartBinding
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.cart.model.OfferApplied
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNull
import javax.inject.Singleton

@Singleton
class CartAdapter(cartItemList: List<CartItem>) :
    RecyclerView.Adapter<CartAdapter.CustomViewHolder>() {
    var cartList = cartItemList
    var onItemDetailClicked: ((productId: String) -> Unit)? = null
    var onItemDeleteClicked: ((productId: String, cartItem: CartItem) -> Unit)? = null
    var onOfferClicked: ((offerAppliedList: OfferApplied, productName: String, productSku: String) -> Unit)? = null
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
            onItemDetailClicked?.invoke(cartList[position].product_id.toString())
        }
        holder.binding.ivDelete.setOnClickListener {
            onItemDeleteClicked?.invoke(cartList[position].product_id.toString(), cartList[position])
        }
        holder.binding.tvOffersApplied.setOnClickListener {
            if (cartList[position].offer_applied.isNotNull()) {
                onOfferClicked?.invoke(cartList[position].offer_applied, cartList[position].product_name, cartList[position].product_sku )
            }
        }
        holder.binding.ivSubtract.setOnClickListener {
            if (quantity > 1) {
                quantity -= 1
                cartList[position].quantity = quantity
                notifyDataSetChanged()
                onQuantityClicked?.invoke(cartList[position])
            }
        }
        holder.binding.ivAdd.setOnClickListener {
            quantity += 1
            cartList[position].quantity = quantity
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