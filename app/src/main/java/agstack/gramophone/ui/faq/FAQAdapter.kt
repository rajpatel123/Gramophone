package agstack.gramophone.ui.faq

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemFaqBinding
import agstack.gramophone.ui.referandearn.model.GramcashFaqItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class FAQAdapter(faqList: ArrayList<GramcashFaqItem>) :
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

        var model: GramcashFaqItem = mfaqList[position]!!
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ItemFaqBinding

        holder.binding.answerLL.removeAllViews()
        if (model.answer?.size!! > 0) {
            model?.answer?.forEach {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_answer, null, false)
                val tv = view.findViewById<TextView>(R.id.tv_answer)
                tv.setText(it)
                holder.binding.answerLL.addView(view)
            }
        }
        mBinding.lltop.setOnClickListener {

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