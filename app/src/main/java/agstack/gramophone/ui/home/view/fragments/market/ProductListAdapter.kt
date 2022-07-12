package agstack.gramophone.ui.home.view.fragments.market


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemFeatureProductBinding
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductListAdapter(list: ArrayList<ProductData>) :
    RecyclerView.Adapter<ProductListAdapter.CustomViewHolder>() {
    var productList = list
    lateinit var mContext: Context
    var selectedProduct: ((ProductData) -> Unit)? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemFeatureProductBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {

        var productModel: ProductData = productList[i]
        holder.binding.setVariable(BR.model, productModel)
        val mBinding = holder.binding as ItemFeatureProductBinding
        mBinding.tvProductDescription.text= "RICE"

        /* if (productModel.icon != null)
             Glide.with(mContext).asBitmap().load(productModel.icon).into(mBinding.ivProductIcon)
 */
        mBinding.productDetailsContainer.setOnClickListener {
            selectedProduct?.invoke(productModel)
        }


    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class CustomViewHolder(var binding: ItemFeatureProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}