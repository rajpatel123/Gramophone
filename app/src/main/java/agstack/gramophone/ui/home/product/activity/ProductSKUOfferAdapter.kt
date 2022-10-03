package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemAvailableOffersBinding
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNull
import kotlin.math.roundToInt

class ProductSKUOfferAdapter(
    SKUOfferList: ArrayList<PromotionListItem?>,
    val selectedSkuPrice: Float,
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
        var amountSaved = 0f
        if (model.amount_saved.isNotNull()) {
            amountSaved = model.amount_saved!!.toFloat()
        }

        if (model.amount_saved.isNotNull() && selectedSkuPrice > model.amount_saved!!.toFloat()) {
            val payOnly = selectedSkuPrice - amountSaved
            val payOnlyString: String = if (payOnly.toString().contains(".0") || payOnly.toString().contains(".00")) {
                payOnly.roundToInt().toString()
            } else {
                payOnly.toString()
            }
            val selectedSkuPriceString: String =
                if (selectedSkuPrice.toString().contains(".0") || selectedSkuPrice.toString().contains(".00")) {
                    selectedSkuPrice.roundToInt().toString()
                } else {
                    selectedSkuPrice.toString()
                }
            mBinding.tvPayOnly.text = holder.itemView.context.getString(R.string.rupee) + payOnlyString
            mBinding.tvSkuPrice.text = holder.itemView.context.getString(R.string.rupee) + selectedSkuPriceString

            mBinding.tvPayOnlyTitle.visibility = View.VISIBLE
            mBinding.tvPayOnly.visibility = View.VISIBLE
            mBinding.tvSkuPrice.visibility = View.VISIBLE
        } else {
            mBinding.tvPayOnlyTitle.visibility = View.GONE
            mBinding.tvPayOnly.visibility = View.GONE
            mBinding.tvSkuPrice.visibility = View.GONE
        }
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