package agstack.gramophone.ui.home.product.fragment


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemRelatedProductFragmentBinding
import agstack.gramophone.ui.home.view.fragments.market.model.RelatedProductItem
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RelatedProductFragmentAdapter(relatedProductItemList: List<RelatedProductItem>) :
    RecyclerView.Adapter<RelatedProductFragmentAdapter.CustomViewHolder>() {
    var mRelatedProductItemList = relatedProductItemList
    var selectedProduct: ((RelatedProductItem) -> Unit)? = null



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemRelatedProductFragmentBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val model: RelatedProductItem = mRelatedProductItemList[position]
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ItemRelatedProductFragmentBinding

        if (!TextUtils.isEmpty(model.salesPrice) && !model.salesPrice.equals("0")){
            holder.binding.productPrice.setText("\u20B9" + model.salesPrice)
        }else{
            holder.binding.productPrice.visibility= INVISIBLE
        }
        mBinding.productDetailsContainer.setOnClickListener{
            selectedProduct?.invoke(model)
        }

    }

    override fun getItemCount(): Int {
        return mRelatedProductItemList.size
    }

    inner class CustomViewHolder(var binding: ItemRelatedProductFragmentBinding) :
        RecyclerView.ViewHolder(binding.root)
}