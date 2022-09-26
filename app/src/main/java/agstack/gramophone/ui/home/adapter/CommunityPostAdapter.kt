package agstack.gramophone.ui.home.adapter

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.*
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.Data
import agstack.gramophone.utils.Constants.BLOCK_USER
import agstack.gramophone.utils.Constants.COPY_POST
import agstack.gramophone.utils.Constants.DELETE_POST
import agstack.gramophone.utils.Constants.EDIT_POST
import agstack.gramophone.utils.Constants.PIN_POST
import agstack.gramophone.utils.Constants.REPORT_POST
import agstack.gramophone.utils.IntentKeys
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.util.*


/**
 * [RecyclerView.Adapter] holding [NestedRecyclerFragment] view pager logic
 */
class CommunityPostAdapter(val dataList: List<agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.Data>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewPageStates = HashMap<Int, Int>()
    private lateinit var context: Context
    var onItemDetailClicked: ((postId: String) -> Unit)? = null
    var onItemLikesClicked: ((postId: String) -> Unit)? = null
    var onItemCommentsClicked: ((postId: String) -> Unit)? = null
    var onShareClicked: ((type: String) -> Unit)? = null
    var onTripleDotMenuClicked: ((type: String) -> Unit)? = null
    var onMenuOptionClicked: ((type: Data) -> Unit)? = null
    var onLikeClicked: ((post: Data) -> Unit)? = null
    var onBookMarkClicked: ((post: Data) -> Unit)? = null
    lateinit var setImage: SetImage
    var lastSelectPosition: Int = 0

    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context = parent.context
        return TextItemHolder(ItemPostBinding.inflate(LayoutInflater.from(context)))


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val textHolder = holder as TextItemHolder
        configureItem(textHolder, position)
    }

    private fun configureItem(holder: TextItemHolder, position: Int) {
        val data = dataList?.get(position)
        holder.itemPostBinding.setVariable(BR.model, data)
        val mBinding = holder.itemPostBinding
        data?.position = position

        holder.itemPostBinding.tvDesc.setOnClickListener {
            onItemDetailClicked?.invoke(data?._id!!)
        }

        if (data?.author?.photoUrl != null) {
            setImage.onImageSet(data.author.photoUrl,holder.itemPostBinding.ivProfileImage)
        }

        if (data?.images?.size!! >0) {
            setImage.onImageSet(data?.images[0].url, holder.itemPostBinding.postImage)
            holder.itemPostBinding.imageContainer.visibility = VISIBLE
            holder.itemPostBinding.postImage.minimumHeight = 800
        } else {
            holder.itemPostBinding.imageContainer.visibility = INVISIBLE
            holder.itemPostBinding.postImage.minimumHeight = 10

        }


        if (data?.lastComment != null) {
            if (data.lastComment.author.size > 0 && data?.lastComment.author[0].photoUrl != null) {
                setImage.onImageSet(
                    data?.lastComment.author[0].photoUrl,
                    holder.itemPostBinding.ivCommentUsersProfile
                )
            }

            if (data.lastComment.author.size > 0) {
                holder.itemPostBinding.tvName1.setText("" + data.lastComment.author[0].username)
            }

        }


        Log.d("Raj", ""+data?.liked)
        if (data.liked) {
            holder.itemPostBinding.ivLike.setImageResource(R.drawable.ic_liked)
        } else {
            holder.itemPostBinding.ivLike.setImageResource(R.drawable.ic_like)
        }

        if (data.bookMarked) {
            holder.itemPostBinding.ivBookmark.setImageResource(R.drawable.ic_bookmarked)
        } else {
            holder.itemPostBinding.ivBookmark.setImageResource(R.drawable.ic_bookmark)
            }

            holder.itemPostBinding.ivMenu.setOnClickListener {
                dataList?.get(lastSelectPosition)?.isSelected = false
                lastSelectPosition = position
                data?.isSelected = true
                notifyDataSetChanged()
                onTripleDotMenuClicked?.invoke(data?._id!!)

            }


        holder.itemPostBinding.postImage.setOnClickListener {
            onItemDetailClicked?.invoke(data._id)
        }
            holder.itemPostBinding.ivLike.setOnClickListener {
                onLikeClicked?.invoke(data)
            }

            holder.itemPostBinding.tvLikes.setOnClickListener {
                onItemLikesClicked?.invoke(data?._id!!)
            }
            holder.itemPostBinding.rlComments.setOnClickListener {
                onItemCommentsClicked?.invoke(data?._id!!)
            }
            holder.itemPostBinding.llComment.setOnClickListener {
                onItemCommentsClicked?.invoke(data?._id!!)
            }
            holder.itemPostBinding.ivWhatsapp.setOnClickListener {
                onShareClicked?.invoke(data.link_url)
        }


        holder.itemPostBinding.ivBookmark.setOnClickListener {
            onBookMarkClicked?.invoke(data)
        }

        holder.itemPostBinding.llEdit.setOnClickListener {
            data.menu= EDIT_POST
            onMenuOptionClicked?.invoke(data)
        }
        holder.itemPostBinding.llPinPost.setOnClickListener {
            data.menu= PIN_POST
            onMenuOptionClicked?.invoke(data)

        }

        holder.itemPostBinding.llDelete.setOnClickListener {
            data.menu= DELETE_POST
            onMenuOptionClicked?.invoke(data)
        }

        holder.itemPostBinding.llReport.setOnClickListener {
            data.menu= REPORT_POST
            onMenuOptionClicked?.invoke(data)
        }

        holder.itemPostBinding.llBlock.setOnClickListener {
            data.menu= BLOCK_USER
            onMenuOptionClicked?.invoke(data)
        }

        holder.itemPostBinding.llCopy.setOnClickListener {
            data.menu= COPY_POST
            onMenuOptionClicked?.invoke(data)
        }
    }

    private fun configurePagerHolder(holder: PagerItemHolder, position: Int) {
//        val data = dataList?.get(position)
//        val adapter = BannerViewPagerAdapter(data.pagerItemList, context)
//
//        holder.bindingBannerImageBinding.bannerPager.adapter = adapter
//        holder.bindingBannerImageBinding?.dotsIndicator?.attachTo(holder.bindingBannerImageBinding?.bannerPager!!)
//        if (viewPageStates.containsKey(position)) {
//            viewPageStates[position]?.let {
//                holder.bindingBannerImageBinding.bannerPager.currentItem = it
//            }
//        }
    }


        fun getItem(position: Int): Data? {
            return dataList?.get(position) ?: null
        }

        private fun configureVideoHolder(holder: VideoHolder, position: Int) {
            val data = dataList?.get(position)
            holder.itemPostVideoBinding.setVariable(BR.model, data)
            val mBinding = holder.itemPostVideoBinding
        }

        override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
            if (holder is PagerItemHolder) {
                viewPageStates.put(
                holder.absoluteAdapterPosition,
                holder.bindingBannerImageBinding.bannerPager.currentItem
            )
            super.onViewRecycled(holder)
        }
    }


        override fun getItemCount(): Int = dataList?.size!!

    companion object {
        internal const val VIEW_TYPE_POST = "POST"
        internal const val VIEW_TYPE_BANNER = "BANNER"
        internal const val FEATURED_PRODUCTS = "FEATURED_PRODUCTS"
        internal const val VIEW_TYPE_QUIZ = "QUIZ"

    }
}


private class TextItemHolder(var itemPostBinding: ItemPostBinding) :
    RecyclerView.ViewHolder(itemPostBinding.root)

private class TextImageHolder(var itemBannerImageBinding: ItemBannerImageBinding) :
    RecyclerView.ViewHolder(itemBannerImageBinding.root)

private class VideoHolder(var itemPostVideoBinding: ItemPostVideoBinding) :
    RecyclerView.ViewHolder(itemPostVideoBinding.root)

private class TextPollHolder(var itemPollBinding: ItemPollBinding) :
    RecyclerView.ViewHolder(itemPollBinding.root)

private class PagerItemHolder(var bindingBannerImageBinding: ItemRecyclerPagerBinding) :
    RecyclerView.ViewHolder(bindingBannerImageBinding.root)
