package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemProgressBinding
import agstack.gramophone.databinding.RatingReviewItemBinding
import agstack.gramophone.ui.home.view.fragments.market.model.ReviewList
import agstack.gramophone.ui.home.view.fragments.market.model.ReviewListItem
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView

class RatingAndReviewsAdapter(reviewList: ArrayList<ReviewListItem?>?, limit: Int? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mReviewList = reviewList
    lateinit var mContext: Context
    val listSizeLimit = limit
    val LOADING_VIEW = 1
    val ITEM_VIEW = 0


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = viewGroup.context
        if (viewType == ITEM_VIEW) {
            return CustomViewHolder(
                RatingReviewItemBinding.inflate(LayoutInflater.from(viewGroup.context))
            )
        } else {
            return LoadingViewHolder(
                ItemProgressBinding.inflate(LayoutInflater.from(viewGroup.context))
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CustomViewHolder) {

            var model: ReviewListItem = mReviewList?.get(position)!!
            model.date = "Position $position"
            holder.binding.setVariable(BR.model, model)
            val mBinding = holder.binding as RatingReviewItemBinding
        } else if (holder is LoadingViewHolder) {
            val binding = holder.binding as ItemProgressBinding
            binding.loadingProgressBar.setIndeterminate(true);
        }
        return

    }

    override fun getItemCount(): Int {
        if (listSizeLimit != null) {
            return listSizeLimit
        } else {
            return mReviewList?.size!!
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (mReviewList?.get(position) == null) return LOADING_VIEW else return ITEM_VIEW
    }

    fun showLoadingItem() {
        val lastItem = itemCount - 1;
        if (mReviewList?.get(lastItem) != null) {
            mReviewList?.add(null)
            notifyItemInserted(lastItem)
        }

    }

    fun hideLoadingItem() {
        val lastItem = itemCount - 1;
        mReviewList?.remove(null)
        notifyItemRemoved(lastItem)
       /* if (mReviewList?.size!! > 0 && mReviewList?.get(lastItem) == null) {
            mReviewList?.removeAt(lastItem)
            notifyItemRemoved(lastItem)
        }*/

    }

    inner class CustomViewHolder(var binding: RatingReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class LoadingViewHolder(val binding: ItemProgressBinding) :
        RecyclerView.ViewHolder(binding.root)


}