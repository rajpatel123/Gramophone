package agstack.gramophone.ui.referralrules

import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemFaqBinding
import agstack.gramophone.databinding.ItemReferalrulesBinding
import agstack.gramophone.ui.faq.FAQAdapter
import agstack.gramophone.ui.referandearn.model.GramcashFaqItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ReferalRulesAdapter(referralRulesList: ArrayList<String>) :
    RecyclerView.Adapter<ReferalRulesAdapter.CustomViewHolder>() {

    val mreferralRulesList = referralRulesList
    lateinit var mContext: Context


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ReferalRulesAdapter.CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemReferalrulesBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: ReferalRulesAdapter.CustomViewHolder, position: Int) {

        var model: String = mreferralRulesList[position]!!
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ItemReferalrulesBinding
        mBinding.tvTitle.text = model



    }

    override fun getItemCount(): Int {
        return mreferralRulesList.size
    }

    inner class CustomViewHolder(var binding: ItemReferalrulesBinding) :
        RecyclerView.ViewHolder(binding.root)

}