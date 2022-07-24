package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.databinding.RatingReviewItemBinding
import agstack.gramophone.ui.home.view.fragments.market.model.ReviewList
import agstack.gramophone.ui.home.view.fragments.market.model.ReviewListItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RatingAndReviewsAdapter(reviewList: ReviewList?,limit:Int?=null) :
    RecyclerView.Adapter<RatingAndReviewsAdapter.CustomViewHolder>() {

    var mReviewList = reviewList
    lateinit var mContext: Context
    val listSizeLimit = limit


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        mContext = viewGroup.context
        return CustomViewHolder(
            RatingReviewItemBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        var model: ReviewListItem = mReviewList?.data?.get(position)!!
        holder.binding.setVariable(BR.model, model)
        val mBinding = holder.binding as RatingReviewItemBinding



    }

    override fun getItemCount(): Int {
        if(listSizeLimit!=null){
            return listSizeLimit
        }else{
        return mReviewList?.data?.size!!}
    }

    inner class CustomViewHolder(var binding: RatingReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}