package agstack.gramophone.ui.home.product.adapter

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemReferalPointsAdapterBinding
import agstack.gramophone.databinding.KeyValueItemBinding
import agstack.gramophone.ui.home.view.fragments.market.model.KeyPointsItem
import agstack.gramophone.ui.referandearn.aboutreferralpoints.adapter.AboutReferalPointsAdapter
import agstack.gramophone.ui.referandearn.aboutreferralpoints.adapter.AnswerListAdapter
import agstack.gramophone.ui.referandearn.model.ReferralPointsItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.color

class KeyValueListAdapter(listatKeyvalue: ArrayList<KeyPointsItem>) :
    RecyclerView.Adapter<KeyValueListAdapter.CustomViewHolder>() {

    val mKeyValueList = listatKeyvalue
    lateinit var mContext: Context


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): KeyValueListAdapter.CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            KeyValueItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: KeyValueListAdapter.CustomViewHolder, position: Int) {

        val model: KeyPointsItem = mKeyValueList[position]
        //holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as KeyValueItemBinding
        if (position % 2 == 0) {
            mBinding.tvKey.setBackgroundResource(R.drawable.greensolid_with_grey_borders)
            mBinding.tvValue.setBackgroundResource(R.drawable.greensolid_with_grey_borders)
        }

        mBinding.tvKey.text = model.value
        mBinding.tvValue.text = model.key




    }

    override fun getItemCount(): Int {
        return mKeyValueList.size
    }

    inner class CustomViewHolder(var binding: KeyValueItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}