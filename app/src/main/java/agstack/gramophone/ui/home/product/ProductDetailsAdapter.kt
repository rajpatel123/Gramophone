package agstack.gramophone.ui.home.product

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ProductDetailsRowItemBinding
import agstack.gramophone.ui.home.view.fragments.market.model.KeyPointsItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductDetailsAdapter(productDetailsKeyValueList: ArrayList<KeyPointsItem?>) :
    RecyclerView.Adapter<ProductDetailsAdapter.CustomViewHolder>() {

    val mProductDetailsKeyValueList = productDetailsKeyValueList
    lateinit var mContext: Context
    public var isShowMoreSelected: Boolean = false


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ProductDetailsAdapter.CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ProductDetailsRowItemBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: ProductDetailsAdapter.CustomViewHolder, position: Int) {
        var model: KeyPointsItem = mProductDetailsKeyValueList[position]!!
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ProductDetailsRowItemBinding
        if (position % 2 == 0) {
            mBinding.mainContainer.setBackgroundResource(R.drawable.greensolid_with_grey_borders)
        }
    }

    override fun getItemCount(): Int {
        if (isShowMoreSelected) {
            return mProductDetailsKeyValueList.size
        } else {
            return 2
        }
    }

    inner class CustomViewHolder(var binding: ProductDetailsRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}
