package agstack.gramophone.ui.home.adapter


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemFeaturedProductBinding
import agstack.gramophone.ui.home.view.fragments.market.model.Data
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FeaturedProductListAdapter(
    list: List<Data>,
    private val listener: (Int) -> Unit,
) :
    RecyclerView.Adapter<FeaturedProductListAdapter.CustomViewHolder>() {
    var featuredProductList = list
    lateinit var mContext: Context


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemFeaturedProductBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {
        val productModel: Data = featuredProductList[i]
        holder.binding.setVariable(BR.model, productModel)
        val mBinding = holder.binding as ItemFeaturedProductBinding

        mBinding.productDetailsContainer.setOnClickListener {
            listener.invoke(productModel.product_id)
        }
    }

    override fun getItemCount(): Int {
        return featuredProductList.size
    }

    inner class CustomViewHolder(var binding: ItemFeaturedProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}