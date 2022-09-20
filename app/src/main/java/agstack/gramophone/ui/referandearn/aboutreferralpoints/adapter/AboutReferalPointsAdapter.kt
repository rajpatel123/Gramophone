package agstack.gramophone.ui.referandearn.aboutreferralpoints.adapter

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemReferalPointsAdapterBinding
import agstack.gramophone.ui.referandearn.model.ReferralPointsItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.color

class AboutReferalPointsAdapter(arrayList: ArrayList<ReferralPointsItem>) :
    RecyclerView.Adapter<AboutReferalPointsAdapter.CustomViewHolder>() {

    val mReferralPointsList = arrayList
    lateinit var mContext: Context


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): AboutReferalPointsAdapter.CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemReferalPointsAdapterBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: AboutReferalPointsAdapter.CustomViewHolder, position: Int) {

        var model: ReferralPointsItem = mReferralPointsList[position]!!
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ItemReferalPointsAdapterBinding
        if(position==0){
            mBinding.tvTitle.setTextColor(mContext.color(R.color.blakish))
        }
        if(position==1){
            mBinding.tvTitle.setTextColor(mContext.color(R.color.green_dark))
        }
        if(position==2){
            mBinding.tvTitle.setTextColor(mContext.color(R.color.red))
        }
        mBinding.rvAnswerList.adapter=AnswerListAdapter(model.answer as ArrayList<String>)



    }

    override fun getItemCount(): Int {
        return mReferralPointsList.size
    }

    inner class CustomViewHolder(var binding: ItemReferalPointsAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

}