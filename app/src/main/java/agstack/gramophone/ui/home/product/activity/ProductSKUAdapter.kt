package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemRadioProductPackingBinding
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductSKUAdapter(
    SKUList: ArrayList<ProductSkuListItem?>,
    val selectedSKUProduct: ((ProductSkuListItem) -> Unit)?,
) :
    RecyclerView.Adapter<ProductSKUAdapter.CustomViewHolder>() {
    var mSKUList = SKUList
    lateinit var mContext: Context
    var selectedProduct: ((ProductSkuListItem) -> Unit)? = null
    var lastSelectPosition: Int = 0


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemRadioProductPackingBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        var model: ProductSkuListItem = mSKUList[position]!!
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ItemRadioProductPackingBinding

        mBinding.skuLL.setOnClickListener {
            if (lastSelectPosition == 0) {
                for (item in mSKUList) {
                    item?.selected = false
                }
            }
            mSKUList[lastSelectPosition]?.selected = false
            lastSelectPosition = position
            model.selected = true

            selectedProduct?.invoke(model)
            selectedSKUProduct?.invoke(model)
            notifyDataSetChanged()
        }

        mBinding.radioBtn.setOnClickListener {
            if (lastSelectPosition == 0) {
                for (item in mSKUList) {
                    item?.selected = false
                }
            }
            mSKUList[lastSelectPosition]?.selected = false
            lastSelectPosition = position
            model.selected = true

            selectedProduct?.invoke(model)
            selectedSKUProduct?.invoke(model)
            notifyDataSetChanged()
        }


    }

    override fun getItemCount(): Int {
        return mSKUList.size
    }

    inner class CustomViewHolder(var binding: ItemRadioProductPackingBinding) :
        RecyclerView.ViewHolder(binding.root)
}