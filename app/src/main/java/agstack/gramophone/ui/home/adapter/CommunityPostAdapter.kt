package agstack.gramophone.ui.home.adapter

import agstack.gramophone.BR
import agstack.gramophone.databinding.*
import agstack.gramophone.ui.home.view.fragments.community.model.Data
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context = parent.context
        return when (viewType) {
            VIEW_TYPE_TEXT ->
                TextItemHolder(ItemPostBinding.inflate(LayoutInflater.from(context)))
            VIEW_TYPE_POLL ->
                TextPollHolder(ItemPollBinding.inflate(LayoutInflater.from(context)))
            VIEW_TYPE_IMAGE_TEXT ->
                TextImageHolder(ItemBannerImageBinding.inflate(LayoutInflater.from(context)))
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
            onShareOrBookMarkClicked?.invoke(data.textItem!!)
        }

        holder.itemBannerImageBinding.ivFacebook.setOnClickListener {
            onShareOrBookMarkClicked?.invoke(data.textItem!!)
        }

        holder.itemBannerImageBinding.ivBookmark.setOnClickListener {
            onShareOrBookMarkClicked?.invoke(data.textItem!!)
        }
    }

    private fun configureTextItem(holder: TextItemHolder, position: Int) {
        val data = dataList[position]
        holder.itemPostBinding.setVariable(BR.model, data)
        val mBinding = holder.itemPostBinding

        holder.itemPostBinding.tvDesc.setOnClickListener {
            onItemDetailClicked?.invoke(data.textItem!!)
        }

        holder.itemPostBinding.rlLikes.setOnClickListener {
            onItemLikesClicked?.invoke(data.textItem!!)
        }
        holder.itemPostBinding.rlComments.setOnClickListener {
            onItemCommentsClicked?.invoke(data.textItem!!)
        }
        holder.itemPostBinding.ivWhatsapp.setOnClickListener {
            onShareOrBookMarkClicked?.invoke(data.textItem!!)
        }

        holder.itemPostBinding.ivFacebook.setOnClickListener {
            onShareOrBookMarkClicked?.invoke(data.textItem!!)
        }

        holder.itemPostBinding.ivBookmark.setOnClickListener {
            onShareOrBookMarkClicked?.invoke(data.textItem!!)
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
        internal const val VIEW_TYPE_QUIZ = 55
    }
}


private class TextItemHolder(var itemPostBinding: ItemPostBinding) :
    RecyclerView.ViewHolder(itemPostBinding.root)

private class TextImageHolder(var itemBannerImageBinding: ItemBannerImageBinding) :
    RecyclerView.ViewHolder(itemBannerImageBinding.root)

private class TextPollHolder(var itemPollBinding: ItemPollBinding) :
    RecyclerView.ViewHolder(itemPollBinding.root)

private class PagerItemHolder(var bindingBannerImageBinding: ItemRecyclerPagerBinding) :
    RecyclerView.ViewHolder(bindingBannerImageBinding.root)
