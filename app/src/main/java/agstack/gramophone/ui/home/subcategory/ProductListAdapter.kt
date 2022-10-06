package agstack.gramophone.ui.home.subcategory

import agstack.gramophone.databinding.ItemProgressBinding
import agstack.gramophone.databinding.ItemSubCategoryProductListBinding
import agstack.gramophone.ui.home.view.fragments.market.model.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Singleton

@Singleton
class ProductListAdapter(private val productDataList: List<Data>?) :
    RecyclerView.Adapter<ProductListAdapter.CustomViewHolder>() {
    var onAddToCartClick: ((productId: Int) -> Unit)? = null
    var onProductDetailClick: ((productId: Int) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemSubCategoryProductListBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.model = productDataList!![position]
        holder.binding.tvAddToCart.setOnClickListener {
            onAddToCartClick?.invoke(productDataList[position].product_id)
        }
        holder.binding.ivProduct.setOnClickListener {
            onProductDetailClick?.invoke(productDataList[position].product_id)
        }
        holder.binding.tvProductName.setOnClickListener {
            onProductDetailClick?.invoke(productDataList[position].product_id)
        }
        holder.binding.llPrice.setOnClickListener {
            onProductDetailClick?.invoke(productDataList[position].product_id)
        }
    }

    override fun getItemCount(): Int {
        return if (productDataList.isNullOrEmpty())
            0
        else productDataList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    /*fun showLoadingItem() {
        val lastItem = itemCount - 1;
        if (productDataList?.get(lastItem) != null) {
            productDataList.add(null)
            notifyItemInserted(lastItem + 1)
        }

    }

    fun hideLoadingItem() {
        val lastItem = itemCount - 1;
        productDataList?.remove(null)
        notifyItemRemoved(lastItem)


    }*/

    inner class CustomViewHolder(var binding: ItemSubCategoryProductListBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class LoadingViewHolder(val binding: ItemProgressBinding) :
        RecyclerView.ViewHolder(binding.root)
}