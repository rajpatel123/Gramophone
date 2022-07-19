package agstack.gramophone.ui.cart.adapter


import agstack.gramophone.R
import agstack.gramophone.databinding.ItemCartBinding
import agstack.gramophone.ui.cart.model.CartItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Singleton

@Singleton
class CartAdapter(cartItemList: List<CartItem>) :
    RecyclerView.Adapter<CartAdapter.CustomViewHolder>() {
    var cartList = cartItemList
    var selectedProduct: ((CartItem) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemCartBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.model = cartList[position]
        holder.binding.ivProduct.setOnClickListener {

            selectedProduct?.invoke(cartList[position])
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

    fun updateAdapter(cartItems: List<CartItem>) {
        cartList = cartItems
        notifyDataSetChanged();
    }

    class OnClickListener(
        val productListener: (cartItem: CartItem) -> Unit,
//        val removeListener: (productId: String) -> Unit
    ) {
        fun onProductClick(cartItem: CartItem) = productListener(cartItem)
        fun onRemoveClick(cartItem: CartItem) = productListener(cartItem)
    }

    inner class CustomViewHolder(var binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)
}