package agstack.gramophone.ui.home.subcategory

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemAvailableOffersOnParticularProductBinding
import agstack.gramophone.ui.home.subcategory.model.Offer
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNull
import kotlin.math.roundToInt

class AvailableProductOffersAdapter(
    SKUOfferList: ArrayList<Offer>,
    private val selectedSkuPrice: Float,
    private val selectedOfferProduct: ((Offer) -> Unit)?,
    private val onOfferDetailClicked: ((Offer) -> Unit)?,
) :
    RecyclerView.Adapter<AvailableProductOffersAdapter.CustomViewHolder>() {
    var mSKUOfferList = SKUOfferList
    lateinit var mContext: Context
    var lastSelectPosition: Int = 0


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemAvailableOffersOnParticularProductBinding.inflate(LayoutInflater.from(viewGroup.context),
                viewGroup,
                false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val model: Offer = mSKUOfferList[position]
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ItemAvailableOffersOnParticularProductBinding
        var amountSaved = 0f
        if (model.benefitinrupes.isNotNull()) {
            amountSaved = model.benefitinrupes.toFloat()
        }

        if (model.benefitinrupes.isNotNull() && model.benefitinrupes > 0f && selectedSkuPrice > model.benefitinrupes.toFloat()) {
            val payOnly = selectedSkuPrice - amountSaved
            val payOnlyString: String =
                if (payOnly.toString().contains(".0") || payOnly.toString().contains(".00")) {
                    payOnly.roundToInt().toString()
                } else {
                    payOnly.toString()
                }
            val selectedSkuPriceString: String =
                if (selectedSkuPrice.toString().contains(".0") || selectedSkuPrice.toString()
                        .contains(".00")
                ) {
                    selectedSkuPrice.roundToInt().toString()
                } else {
                    selectedSkuPrice.toString()
                }
            mBinding.tvPayOnly.text =
                holder.itemView.context.getString(R.string.rupee) + payOnlyString
            mBinding.tvSkuPrice.text =
                holder.itemView.context.getString(R.string.rupee) + selectedSkuPriceString

            mBinding.viewPriceSeparator.visibility = View.VISIBLE
            mBinding.tvPayOnlyTitle.visibility = View.VISIBLE
            mBinding.tvPayOnly.visibility = View.VISIBLE
            mBinding.tvSkuPrice.visibility = View.VISIBLE
        } else {
            mBinding.viewPriceSeparator.visibility = View.GONE
            mBinding.tvPayOnlyTitle.visibility = View.GONE
            mBinding.tvPayOnly.visibility = View.GONE
            mBinding.tvSkuPrice.visibility = View.GONE
        }
        mBinding.radioBtn.setOnClickListener {
            mSKUOfferList[lastSelectPosition].selected = false
            lastSelectPosition = position
            model.selected = true
            selectedOfferProduct?.invoke(model)
            notifyDataSetChanged()
        }
        mBinding.tvViewdetail.setOnClickListener {
            onOfferDetailClicked?.invoke(model)
        }
    }

    override fun getItemCount(): Int {
        return mSKUOfferList.size
    }

    inner class CustomViewHolder(var binding: ItemAvailableOffersOnParticularProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}