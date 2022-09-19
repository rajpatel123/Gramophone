package agstack.gramophone.ui.home.adapter

import agstack.gramophone.BR
import agstack.gramophone.databinding.*
import agstack.gramophone.ui.home.view.fragments.community.model.likes.Data
import agstack.gramophone.utils.Constants.BLOCK_USER
import agstack.gramophone.utils.Constants.COPY_POST
import agstack.gramophone.utils.Constants.DELETE_POST
import agstack.gramophone.utils.Constants.EDIT_POST
import agstack.gramophone.utils.Constants.REPORT_POST
import agstack.gramophone.utils.IntentKeys
import agstack.gramophone.utils.IntentKeys.Companion.FacebookShareKey
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*


/**
 * [RecyclerView.Adapter] holding [NestedRecyclerFragment] view pager logic
 */
class CommunityPostAdapter(private val dataList: ArrayList<Data>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewPageStates = HashMap<Int, Int>()
    private lateinit var context: Context
    var onItemDetailClicked: ((postId: String) -> Unit)? = null
    var onItemLikesClicked: ((postId: String) -> Unit)? = null
    var onItemCommentsClicked: ((postId: String) -> Unit)? = null
    var onShareOrBookMarkClicked: ((type: String) -> Unit)? = null
    var onTripleDotMenuClicked: ((type: String) -> Unit)? = null
    var onMenuOptionClicked: ((type: String) -> Unit)? = null

    var lastSelectPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context = parent.context
        return when (viewType) {
            VIEW_TYPE_TEXT ->
                TextItemHolder(ItemPostBinding.inflate(LayoutInflater.from(context)))
            VIEW_TYPE_POLL ->
                TextPollHolder(ItemPollBinding.inflate(LayoutInflater.from(context)))
            VIEW_TYPE_IMAGE_TEXT ->
                TextImageHolder(ItemBannerImageBinding.inflate(LayoutInflater.from(context)))
            VIEW_TYPE_VIDEO ->
                VideoHolder(ItemPostVideoBinding.inflate(LayoutInflater.from(context)))
            else ->
                PagerItemHolder(ItemRecyclerPagerBinding.inflate(LayoutInflater.from(context)))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_TEXT -> {
                val textHolder = holder as TextItemHolder
                configureTextItem(textHolder, position)
            }
            VIEW_TYPE_IMAGE_TEXT -> {
                val textHolder = holder as TextImageHolder
                configureTextImageItem(textHolder, position)
            }

            VIEW_TYPE_POLL -> {
                val textHolder = holder as TextPollHolder
                configureTextPollItem(textHolder, position)
            }
            VIEW_TYPE_PAGER -> {
                val pagerHolder = holder as PagerItemHolder
                configurePagerHolder(pagerHolder, position)
            }

            VIEW_TYPE_VIDEO -> {
                val pagerHolder = holder as VideoHolder
                configureVideoHolder(pagerHolder, position)
            }
        }

    }

    private fun configureTextPollItem(holder: TextPollHolder, position: Int) {
        val data = dataList[position]
        holder.itemPollBinding.setVariable(BR.model, data)
        val mBinding = holder.itemPollBinding
    }

    private fun configureTextImageItem(holder: TextImageHolder, position: Int) {
        val data = dataList[position]
        holder.itemBannerImageBinding.setVariable(BR.model, data)
        val mBinding = holder.itemBannerImageBinding

        holder.itemBannerImageBinding.tvDesc.setOnClickListener {
            onItemDetailClicked?.invoke(data.textItem!!)
        }

        holder.itemBannerImageBinding.ivMenu.setOnClickListener {
            dataList[lastSelectPosition].isSelected=false
            lastSelectPosition = position
            data.isSelected = true
            notifyDataSetChanged()
            onTripleDotMenuClicked?.invoke(data.textItem!!)
        }

        holder.itemBannerImageBinding.cvImageBanner.setOnClickListener {
            onItemDetailClicked?.invoke(data.textItem!!)
        }

        holder.itemBannerImageBinding.rlLikes.setOnClickListener {
            onItemLikesClicked?.invoke(data.textItem!!)
        }
        holder.itemBannerImageBinding.rlComments.setOnClickListener {
            onItemCommentsClicked?.invoke(data.textItem!!)
        }
        holder.itemBannerImageBinding.ivWhatsapp.setOnClickListener {
            onShareOrBookMarkClicked?.invoke(IntentKeys.WhatsAppShareKey)
        }

        holder.itemBannerImageBinding.ivFacebook.setOnClickListener {
            onShareOrBookMarkClicked?.invoke(FacebookShareKey)
        }

        holder.itemBannerImageBinding.ivBookmark.setOnClickListener {
            onShareOrBookMarkClicked?.invoke("bookmark")
        }


        holder.itemBannerImageBinding.llEdit.setOnClickListener {
            onMenuOptionClicked?.invoke(EDIT_POST)
        }

        holder.itemBannerImageBinding.llDelete.setOnClickListener {
            onMenuOptionClicked?.invoke(DELETE_POST)
        }

        holder.itemBannerImageBinding.llReport.setOnClickListener {
            onMenuOptionClicked?.invoke(REPORT_POST)
        }

        holder.itemBannerImageBinding.llBlock.setOnClickListener {
            onMenuOptionClicked?.invoke(BLOCK_USER)
        }

        holder.itemBannerImageBinding.llCopy.setOnClickListener {
            onMenuOptionClicked?.invoke(COPY_POST)
        }
    }

    private fun configureTextItem(holder: TextItemHolder, position: Int) {
        val data = dataList[position]
        holder.itemPostBinding.setVariable(BR.model, data)
        val mBinding = holder.itemPostBinding

        holder.itemPostBinding.tvDesc.setOnClickListener {
            onItemDetailClicked?.invoke(data.textItem!!)
        }

        holder.itemPostBinding.ivMenu.setOnClickListener {
            dataList[lastSelectPosition].isSelected=false

            lastSelectPosition = position
            data.isSelected = true
            notifyDataSetChanged()
            onTripleDotMenuClicked?.invoke(data.textItem!!)

        }

        holder.itemPostBinding.rlLikes.setOnClickListener {
            onItemLikesClicked?.invoke(data.textItem!!)
        }
        holder.itemPostBinding.rlComments.setOnClickListener {
            onItemCommentsClicked?.invoke(data.textItem!!)
        }
        holder.itemPostBinding.ivWhatsapp.setOnClickListener {
            onShareOrBookMarkClicked?.invoke(IntentKeys.WhatsAppShareKey)
        }

        holder.itemPostBinding.ivFacebook.setOnClickListener {
            onShareOrBookMarkClicked?.invoke(FacebookShareKey)
        }

        holder.itemPostBinding.ivBookmark.setOnClickListener {
            onShareOrBookMarkClicked?.invoke(data.textItem!!)
        }

        holder.itemPostBinding.llEdit.setOnClickListener {
            onMenuOptionClicked?.invoke(EDIT_POST)
        }

        holder.itemPostBinding.llDelete.setOnClickListener {
            onMenuOptionClicked?.invoke(DELETE_POST)
        }

        holder.itemPostBinding.llReport.setOnClickListener {
            onMenuOptionClicked?.invoke(REPORT_POST)
        }

        holder.itemPostBinding.llBlock.setOnClickListener {
            onMenuOptionClicked?.invoke(BLOCK_USER)
        }

        holder.itemPostBinding.llCopy.setOnClickListener {
            onMenuOptionClicked?.invoke(COPY_POST)
        }
    }

    private fun configurePagerHolder(holder: PagerItemHolder, position: Int) {
        val data = dataList[position]
        val adapter = BannerViewPagerAdapter(data.pagerItemList, context)

        holder.bindingBannerImageBinding.bannerPager.adapter = adapter
        holder.bindingBannerImageBinding?.dotsIndicator?.attachTo(holder.bindingBannerImageBinding?.bannerPager!!)
        if (viewPageStates.containsKey(position)) {
            viewPageStates[position]?.let {
                holder.bindingBannerImageBinding.bannerPager.currentItem = it
            }
        }
    }

    private fun configureVideoHolder(holder: VideoHolder, position: Int) {
        val data = dataList[position]
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

    override fun getItemViewType(position: Int): Int = dataList[position].viewType

    override fun getItemCount(): Int = dataList.size

    companion object {
        internal const val VIEW_TYPE_TEXT = 51
        internal const val VIEW_TYPE_PAGER = 52
        internal const val VIEW_TYPE_IMAGE_TEXT = 53
        internal const val VIEW_TYPE_POLL = 54
        internal const val VIEW_TYPE_VIDEO = 56
        internal const val VIEW_TYPE_QUIZ = 55
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
