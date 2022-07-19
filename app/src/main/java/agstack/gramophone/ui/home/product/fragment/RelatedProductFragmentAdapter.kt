package agstack.gramophone.ui.home.product.fragment


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemRelatedProductFragmentBinding
import agstack.gramophone.ui.home.view.fragments.market.model.RelatedProductItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RelatedProductFragmentAdapter(relatedProductItemList: ArrayList<RelatedProductItem?>) :
    RecyclerView.Adapter<RelatedProductFragmentAdapter.CustomViewHolder>() {
    var mRelatedProductItemList = relatedProductItemList
    lateinit var mContext: Context
    var selectedProduct: ((RelatedProductItem) -> Unit)? = null



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemRelatedProductFragmentBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        var model: RelatedProductItem = mRelatedProductItemList[position]!!
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ItemRelatedProductFragmentBinding
        mBinding.relatedProdDetailsContainer.setOnClickListener{
            selectedProduct?.invoke(model)
        }

    }

    override fun getItemCount(): Int {
        return mRelatedProductItemList.size
    }

    inner class CustomViewHolder(var binding: ItemRelatedProductFragmentBinding) :
        RecyclerView.ViewHolder(binding.root)
}