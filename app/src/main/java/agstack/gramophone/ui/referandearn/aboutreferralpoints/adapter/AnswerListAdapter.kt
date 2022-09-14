package agstack.gramophone.ui.referandearn.aboutreferralpoints.adapter

import agstack.gramophone.BR
import agstack.gramophone.databinding.BulletItemBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AnswerListAdapter(arrayList: ArrayList<String>) :
    RecyclerView.Adapter<AnswerListAdapter.CustomViewHolder>() {

    val mAnswerList = arrayList
    lateinit var mContext: Context


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): AnswerListAdapter.CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            BulletItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: AnswerListAdapter.CustomViewHolder, position: Int) {

        var model: String = mAnswerList[position]!!
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as BulletItemBinding
        mBinding.tvAnswer.setText(model)



    }

    override fun getItemCount(): Int {
        return mAnswerList.size
    }

    inner class CustomViewHolder(var binding: BulletItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}
