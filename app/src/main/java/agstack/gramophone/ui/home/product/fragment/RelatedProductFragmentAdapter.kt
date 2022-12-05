package agstack.gramophone.ui.home.product.fragment


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemRelatedProductFragmentBinding
import agstack.gramophone.ui.home.view.fragments.market.model.RelatedProductItem
import android.content.Context
import android.view.LayoutInflater
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