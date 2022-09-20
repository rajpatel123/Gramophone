package agstack.gramophone.ui.referandearn.referralpoints

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemMyReferralBinding
import agstack.gramophone.ui.referandearn.model.MyReferralsItem
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MyReferralsAdapter(arrayList: ArrayList<MyReferralsItem>) :
    RecyclerView.Adapter<MyReferralsAdapter.CustomViewHolder>() {

    val mMyRefrralList = arrayList
    lateinit var mContext: Context


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyReferralsAdapter.CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            ItemMyReferralBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: MyReferralsAdapter.CustomViewHolder, position: Int) {

        var model: MyReferralsItem = mMyRefrralList[position]
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as ItemMyReferralBinding

        model.profileImage?.let {
            Glide.with(mContext)
                .load(model.profileImage)
                .apply(
                    RequestOptions()
                        .error(R.drawable.ic_person_standing)
                        .centerCrop()
                )
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mBinding.ivProfileImage)
        }




    }

    override fun getItemCount(): Int {
        return mMyRefrralList.size
    }

    inner class CustomViewHolder(var binding: ItemMyReferralBinding) :
        RecyclerView.ViewHolder(binding.root)

}