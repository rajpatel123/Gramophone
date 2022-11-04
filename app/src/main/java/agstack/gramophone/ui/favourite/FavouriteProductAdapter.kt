package agstack.gramophone.ui.favourite

import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemFavouriteBinding
import agstack.gramophone.databinding.ItemFavouriteProductBinding
import agstack.gramophone.databinding.ItemFeaturedProductBinding
import agstack.gramophone.ui.favourite.model.Data
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FavouriteProductAdapter(
    list: List<Data>
) :
    RecyclerView.Adapter<FavouriteProductAdapter.CustomViewHolder>() {
    var featuredProductList = list
    lateinit var mContext: Context
    lateinit var onProductClicked: (Data) -> Unit

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemFavouriteProductBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {
        val productModel: Data = featuredProductList[i]
        holder.binding.setVariable(BR.model, productModel)
        val mBinding = holder.binding as ItemFavouriteProductBinding

        mBinding.productDetailsContainer.setOnClickListener {
            onProductClicked.invoke(productModel)
        }
    }

    override fun getItemCount(): Int {
        return featuredProductList.size
    }

    inner class CustomViewHolder(var binding: ItemFavouriteProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}