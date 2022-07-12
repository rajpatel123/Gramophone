package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemAvailableOffersBinding
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuOfferItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductSKUOfferAdapter(SKUOfferList: ArrayList<ProductSkuOfferItem?>) :
    RecyclerView.Adapter<ProductSKUOfferAdapter.CustomViewHolder>() {
    var mSKUOfferList = SKUOfferList
    lateinit var mContext: Context
    var selectedProduct: ((ProductSkuOfferItem) -> Unit)? = null
    var lastSelectPosition: Int = 0


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemAvailableOffersBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        var model: ProductSkuOfferItem = mSKUOfferList[position]!!
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ItemAvailableOffersBinding
        mBinding.radioBtn.setOnClickListener {
            mSKUOfferList[lastSelectPosition]?.selected = false
            lastSelectPosition = position
            model.selected = true
            notifyDataSetChanged()
            selectedProduct?.invoke(model)
        }


    }

    override fun getItemCount(): Int {
        return mSKUOfferList.size
    }

    inner class CustomViewHolder(var binding: ItemAvailableOffersBinding) :
        RecyclerView.ViewHolder(binding.root)
}