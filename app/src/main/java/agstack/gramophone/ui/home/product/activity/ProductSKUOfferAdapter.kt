package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemAvailableOffersBinding
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductSKUOfferAdapter(
    SKUOfferList: ArrayList<PromotionListItem?>,
    val selectedOfferProduct: ((PromotionListItem) -> Unit)?,
    val onOfferDetailClicked: ((PromotionListItem) -> Unit)?,
) :
    RecyclerView.Adapter<ProductSKUOfferAdapter.CustomViewHolder>() {
    var mSKUOfferList = SKUOfferList
    lateinit var mContext: Context
    var selectedProduct: ((PromotionListItem) -> Unit)? = null
    var onViewAllClicked: ((PromotionListItem) -> Unit)? = null
    var lastSelectPosition: Int = 0


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemAvailableOffersBinding.inflate(LayoutInflater.from(viewGroup.context),
                viewGroup,
                false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        var model: PromotionListItem = mSKUOfferList[position]!!
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ItemAvailableOffersBinding
        mBinding.radioBtn.setOnClickListener {
            mSKUOfferList[lastSelectPosition]?.selected = false
            lastSelectPosition = position
            model.selected = true
            notifyDataSetChanged()
            selectedProduct?.invoke(model)
            selectedOfferProduct?.invoke(model)
        }
        mBinding.tvViewdetail.setOnClickListener {
            onViewAllClicked?.invoke(model)
            onOfferDetailClicked?.invoke(model)
        }


    }

    override fun getItemCount(): Int {
        return mSKUOfferList.size
    }

    inner class CustomViewHolder(var binding: ItemAvailableOffersBinding) :
        RecyclerView.ViewHolder(binding.root)
}