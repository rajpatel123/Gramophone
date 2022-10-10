package agstack.gramophone.ui.orderdetails.adapter


import agstack.gramophone.databinding.ItemOrderedProductBinding
import agstack.gramophone.ui.orderdetails.model.FreeProduct
import agstack.gramophone.ui.orderdetails.model.Product
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNullOrEmpty
import javax.inject.Singleton

@Singleton
class OrderedProductsAdapter(orderedProducts: List<Product>) :
    RecyclerView.Adapter<OrderedProductsAdapter.CustomViewHolder>() {
    var orderedProductsList = orderedProducts
    var onItemDetailClicked: ((Int) -> Unit)? = null
    var onOfferClicked: ((FreeProduct, String) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemOrderedProductBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.model = orderedProductsList[position]
        if (position == orderedProductsList.size - 1) {
            holder.binding.itemSeparator.visibility = View.GONE
        } else {
            holder.binding.itemSeparator.visibility = View.VISIBLE
        }
            holder.binding.ivProduct.setOnClickListener {
                onItemDetailClicked?.invoke(orderedProductsList[position].product_id)
            }
        holder.binding.tvOffersApplied.setOnClickListener {
            if (orderedProductsList[position].free_products.isNotNullOrEmpty()) {
                onOfferClicked?.invoke(orderedProductsList[position].free_products[0], orderedProductsList[position].product_sku)
            }
        }
    }

    override fun getItemCount(): Int {
        return orderedProductsList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CustomViewHolder(var binding: ItemOrderedProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}