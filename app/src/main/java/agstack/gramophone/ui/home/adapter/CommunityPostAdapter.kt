package agstack.gramophone.ui.home.adapter

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.*
import agstack.gramophone.ui.home.view.fragments.community.model.quiz.GpApiResponseData
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.Data
import agstack.gramophone.ui.home.view.fragments.market.model.BannerResponse
import agstack.gramophone.ui.profile.view.ProfileActivity
import agstack.gramophone.ui.userprofile.UserProfileActivity
import agstack.gramophone.utils.Constants.BLOCK_USER
import agstack.gramophone.utils.Constants.DELETE_POST
import agstack.gramophone.utils.Constants.EDIT_POST
import agstack.gramophone.utils.Constants.PIN_POST
import agstack.gramophone.utils.Constants.REPORT_POST
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.utils.SharedPreferencesKeys.UUIdKey
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_tags.view.*
import java.util.*


/**
 * [RecyclerView.Adapter] holding [NestedRecyclerFragment] view pager logic
 */
class CommunityPostAdapter(
    val dataList: List<Data>?,
    isOther: Boolean
//    quizPoll: List<List<GpApiResponseData>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewPageStates = HashMap<Int, Int>()
    private lateinit var context: Context
    var onItemDetailClicked: ((postId: String) -> Unit)? = null
    var onFollowClicked: ((postId: Data) -> Unit)? = null
    var onItemLikesClicked: ((postId: String) -> Unit)? = null
    var onItemCommentsClicked: ((postId: String) -> Unit)? = null
    var onShareClicked: ((type: String) -> Unit)? = null
    var onTripleDotMenuClicked: ((type: String) -> Unit)? = null
    var onMenuOptionClicked: ((type: Data) -> Unit)? = null
    var onLikeClicked: ((post: Data) -> Unit)? = null
    var onBookMarkClicked: ((post: Data) -> Unit)? = null
    var onProfileImageClicked: ((post: Data) -> Unit)? = null
    lateinit var setImage: SetImage
    var lastSelectPosition: Int = 0

    lateinit var inflater: LayoutInflater
    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context = parent.context
        inflater = LayoutInflater.from(context)
        return TextItemHolder(ItemPostBinding.inflate(LayoutInflater.from(context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val textHolder = holder as TextItemHolder


        if (position>5 && position%5==0 && position<20){
            holder.itemPostBinding.pollLayoutLL.visibility= VISIBLE
            holder.itemPostBinding.rlPosts.visibility= GONE
            holder.itemPostBinding.rlPosts.visibility= GONE

            return
        }else{
            holder.itemPostBinding.pollLayoutLL.visibility= GONE
        }

        if (position >1 && (position==2 || position%5==0)){
            var bannerResponse = SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.BANNER_DATA, BannerResponse::class.java
            )
            if (bannerResponse?.gpApiResponseData?.communityBanner!=null && bannerResponse?.gpApiResponseData?.communityBanner!!.size>0 ){
                holder.itemPostBinding.rlBanner.visibility= VISIBLE
                holder.itemPostBinding.rlPosts.visibility= GONE
                configurePagerHolder(textHolder,position,bannerResponse)
                return
            }
        }

        configureItem(textHolder, position)
        holder.itemPostBinding.rlBanner.visibility= GONE
        holder.itemPostBinding.rlPosts.visibility= VISIBLE
    }

    private fun configureItem(holder: TextItemHolder, position: Int) {
        val data = dataList?.get(position)
        holder.itemPostBinding.setVariable(BR.model, data)
        val mBinding = holder.itemPostBinding
        data?.position = position


        if (!TextUtils.isEmpty(data?.description)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.itemPostBinding.tvDesc.text = Html.fromHtml(data?.description,0)
            }else{
                holder.itemPostBinding.tvDesc.text = Html.fromHtml(data?.description)
            }
        }
        holder.itemPostBinding.tvDesc.setOnClickListener {
            onItemDetailClicked?.invoke(data?._id!!)
        }

        if (data?.author?.photoUrl != null) {
            setImage.onImageSet(data.author.photoUrl,holder.itemPostBinding.ivProfileImage)
        }

        val paramsImageContainer: FrameLayout.LayoutParams = holder.itemPostBinding.postImage.getLayoutParams() as FrameLayout.LayoutParams
        if (data?.images?.size!! >0) {
            setImage.onImageSet(data?.images[0].url, holder.itemPostBinding.postImage)
            holder.itemPostBinding.imageContainer.visibility = VISIBLE
            paramsImageContainer.height = 800
            holder.itemPostBinding.postImage.setLayoutParams(paramsImageContainer)
           // holder.itemPostBinding.postImage.minimumHeight = 600
        } else {
            holder.itemPostBinding.imageContainer.visibility = INVISIBLE
            paramsImageContainer.height = 0
            holder.itemPostBinding.postImage.setLayoutParams(paramsImageContainer)

        }

        val params: LinearLayout.LayoutParams = holder.itemPostBinding.tvDesc.getLayoutParams() as LinearLayout.LayoutParams
        if (!TextUtils.isEmpty(data?.description)) {
            params.height = WRAP_CONTENT
            holder.itemPostBinding.tvDesc.setLayoutParams(params)
        } else {
            params.height = 0
            holder.itemPostBinding.tvDesc.setLayoutParams(params)
        }



        holder.itemPostBinding.tvLikes.text=data.likesCount.toString()+" "+context.getString(R.string.like)
        holder.itemPostBinding.tvComment.text=data.commentsCount.toString()+" "+context.getString(R.string.comment_count)
        if (data.tags.isNotEmpty()) {
            holder.itemPostBinding.llTVTag.removeAllViews()
            data.tags.forEach {
                val view = inflater.inflate(R.layout.item_tags, null)
                view.tvTag.setText(it.tag)
                holder.itemPostBinding.llTVTag.addView(view)
            }
        }

        if (data.lastComment.author.isNotEmpty()) {
            if (data.lastComment.author.isNotEmpty() && data.lastComment.author[0].photoUrl != null) {
                setImage.onImageSet(
                    data?.lastComment.author[0].photoUrl,
                    holder.itemPostBinding.ivCommentUsersProfile
                )
            }

            if (data.lastComment.author.size > 0) {
                holder.itemPostBinding.tvName1.setText("" + data.lastComment.author[0].username)
            }
            holder.itemPostBinding.llChat.visibility = VISIBLE

        } else {
            holder.itemPostBinding.llChat.visibility = GONE
        }

        if (data.liked) {
            holder.itemPostBinding.tvLikes.setTextColor(
                ContextCompat.getColor(holder.itemPostBinding.root.context,
                R.color.red))
            holder.itemPostBinding.ivLike.setImageResource(R.drawable.ic_liked)
        } else {
            holder.itemPostBinding.tvLikes.setTextColor(
                ContextCompat.getColor(holder.itemPostBinding.root.context,
                    R.color.gray))
            holder.itemPostBinding.ivLike.setImageResource(R.drawable.ic_like)
        }

        if (data.author.uuid.equals(
                SharedPreferencesHelper.instance?.getString(UUIdKey)
            )
        ) {
            holder.itemPostBinding.llEdit.visibility = VISIBLE
            holder.itemPostBinding.llDelete.visibility = VISIBLE
            holder.itemPostBinding.llReport.visibility = GONE
            holder.itemPostBinding.llBlock.visibility = GONE
            if (data.author.address==null){
               holder.itemPostBinding.tvCity.visibility= VISIBLE
                holder.itemPostBinding.tvCity.setOnClickListener {
                    context.startActivity(Intent(context, UserProfileActivity::class.java))
                }
            }

        } else {
            if (data.author.address==null){
                holder.itemPostBinding.tvCity.visibility= GONE
            }
            holder.itemPostBinding.llReport.visibility = VISIBLE
            holder.itemPostBinding.llBlock.visibility = VISIBLE
            holder.itemPostBinding.llDelete.visibility = GONE
            holder.itemPostBinding.llEdit.visibility = GONE
            holder.itemPostBinding.llBlock.visibility = VISIBLE

        }

        if (data.pinned  && SharedPreferencesHelper.instance?.getString(
                SharedPreferencesKeys.IS_ADMIN).equals("admin")) {
            holder.itemPostBinding.llPinPost.visibility = VISIBLE
        } else {
            holder.itemPostBinding.llPinPost.visibility = GONE
        }

        if (!TextUtils.isEmpty(data.author.communityUserType) && "admin".equals(data.author.communityUserType,true)){
            holder.itemPostBinding.llBlock.visibility = GONE
            holder.itemPostBinding.rlAdminTag.visibility = VISIBLE
        }else{
            holder.itemPostBinding.llBlock.visibility = VISIBLE
            holder.itemPostBinding.rlAdminTag.visibility = GONE
        }
        if (data.bookMarked) {
            holder.itemPostBinding.ivBookmark.setImageResource(R.drawable.ic_bookmarked)
        } else {
            holder.itemPostBinding.ivBookmark.setImageResource(R.drawable.ic_bookmark)
        }

        holder.itemPostBinding.ivMenu.setOnClickListener {
            if (dataList?.get(position)?.isSelected == true){
                showHideMenu(data, show = false, position)
            }else{
                showHideMenu(data, show = true, position)
            }

        }

        holder.itemPostBinding.tvFollow.setOnClickListener {
            onFollowClicked?.invoke(data)

        }

        holder.itemPostBinding.postImage.setOnClickListener {
            onItemDetailClicked?.invoke(data._id)
        }
        holder.itemPostBinding.ivLike.setOnClickListener {
            onLikeClicked?.invoke(data)
        }

        holder.itemPostBinding.ivProfileImage.setOnClickListener {
            onProfileImageClicked?.invoke(data)
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
                onShareClicked?.invoke(data.linkUrl)
        }


        holder.itemPostBinding.ivBookmark.setOnClickListener {
            onBookMarkClicked?.invoke(data)
        }

        holder.itemPostBinding.llEdit.setOnClickListener {
            showHideMenu(data,show=false,position)
            data.menu= EDIT_POST
            onMenuOptionClicked?.invoke(data)
        }
        holder.itemPostBinding.llPinPost.setOnClickListener {
            showHideMenu(data,show=false,position)
            data.menu= PIN_POST
            onMenuOptionClicked?.invoke(data)

        }

        holder.itemPostBinding.llDelete.setOnClickListener {
            showHideMenu(data,show=false,position)
            data.menu= DELETE_POST
            onMenuOptionClicked?.invoke(data)
        }

        holder.itemPostBinding.llReport.setOnClickListener {
            showHideMenu(data,show=false,position)
            data.menu= REPORT_POST
            onMenuOptionClicked?.invoke(data)
        }

        holder.itemPostBinding.llBlock.setOnClickListener {
            showHideMenu(data,show=false,position)
            data.menu= BLOCK_USER
            onMenuOptionClicked?.invoke(data)
        }


    }

    private fun showHideMenu(data: Data, show: Boolean, position: Int) {
        dataList?.get(lastSelectPosition)?.isSelected = false
        lastSelectPosition = position
        data?.isSelected = show
        notifyDataSetChanged()
        onTripleDotMenuClicked?.invoke(data?._id!!)
    }

    private fun configurePagerHolder(
        holder: TextItemHolder,
        position: Int,
        bannerResponse: BannerResponse?
    ) {

        val data = dataList?.get(position)
        val adapter =
            BannerViewPagerAdapter(bannerResponse?.gpApiResponseData?.communityBanner!!, context)

        holder.itemPostBinding.bannerPager.adapter = adapter
        holder.itemPostBinding?.dotsIndicator?.attachTo(holder.itemPostBinding?.bannerPager!!)
        if (viewPageStates.containsKey(position)) {
            viewPageStates[position]?.let {
                holder.itemPostBinding.bannerPager.currentItem = it
            }
        }
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
