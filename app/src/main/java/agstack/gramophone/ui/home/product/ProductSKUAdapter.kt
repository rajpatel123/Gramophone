package agstack.gramophone.ui.home.product

import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemRadioProductPackingBinding
import agstack.gramophone.ui.home.product.model.ProductWeightPriceModel
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductSKUAdapter(SKUList: ArrayList<ProductWeightPriceModel>) :
    RecyclerView.Adapter<ProductSKUAdapter.CustomViewHolder>() {
    var mSKUList = SKUList
    lateinit var mContext: Context
    var selectedProduct: ((ProductWeightPriceModel) -> Unit)? = null
    var lastSelectPosition: Int = 0


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemRadioProductPackingBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        var model: ProductWeightPriceModel = mSKUList[position]
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ItemRadioProductPackingBinding
        mBinding.radioBtn.setOnClickListener {
            mSKUList[lastSelectPosition].selected = false
            lastSelectPosition = position
            model.selected = true
            notifyDataSetChanged()
            selectedProduct?.invoke(model)
        }


    }

    override fun getItemCount(): Int {
        return mSKUList.size
    }

    inner class CustomViewHolder(var binding: ItemRadioProductPackingBinding) :
        RecyclerView.ViewHolder(binding.root)
}