package agstack.gramophone.ui.faq

import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemFaqBinding
import agstack.gramophone.ui.faq.model.FAQModel
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class FAQAdapter(faqList: ArrayList<FAQModel>) :
    RecyclerView.Adapter<FAQAdapter.CustomViewHolder>() {

    val mfaqList = faqList
    lateinit var mContext: Context


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): FAQAdapter.CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemFaqBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: FAQAdapter.CustomViewHolder, position: Int) {

        var model: FAQModel = mfaqList[position]!!
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ItemFaqBinding

        mBinding.ivArrow.setOnClickListener {

            model.isExpanded = !(model.isExpanded)

            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int {
        return mfaqList.size
    }

    inner class CustomViewHolder(var binding: ItemFaqBinding) :
        RecyclerView.ViewHolder(binding.root)

}