package agstack.gramophone.ui.home.adapter

import agstack.gramophone.R
import agstack.gramophone.ui.home.view.fragments.community.SquareViewPager
import agstack.gramophone.ui.home.view.fragments.community.model.Data
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import okhttp3.internal.immutableListOf
import java.util.*


/**
 * [RecyclerView.Adapter] holding [NestedRecyclerFragment] view pager logic
 */
class CommunityPostAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewPageStates = HashMap<Int, Int>()

    var dataList = immutableListOf<Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return  TextItemHolder(inflater.inflate(R.layout.item_post, parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val textHolder = holder as TextItemHolder
            configureTextItem(textHolder, position)
    }

    private fun configureTextItem(holder: TextItemHolder, position: Int) {

        when {
            position==4 -> {
                holder.banner_layout.visibility= GONE
                holder.tvDesc.visibility=GONE
                holder.poll.visibility= VISIBLE
                holder.llChat.visibility= GONE
                holder.llActions.visibility= GONE
                holder.tvTag.visibility= GONE

            }
            position%2==0 -> {
                holder.banner_layout.visibility=VISIBLE
                holder.tvDesc.visibility=GONE
                holder.poll.visibility= GONE
                holder.llChat.visibility= VISIBLE
                holder.llActions.visibility= VISIBLE
                holder.tvTag.visibility= VISIBLE

            }
            else -> {
                holder.banner_layout.visibility= GONE
                holder.tvDesc.visibility=VISIBLE
                holder.poll.visibility= GONE
                holder.llChat.visibility= VISIBLE
                holder.llActions.visibility= VISIBLE
                holder.tvTag.visibility= VISIBLE

            }
        }


    }

    private fun configurePagerHolder(holder: PagerItemHolder, position: Int) {
//        val data = dataList[position]
//        val adapter = BannerViewPagerAdapter(data.pagerItemList, context)
//        holder.viewPager.adapter = adapter
//
//        if (viewPageStates.containsKey(position)) {
//            viewPageStates[position]?.let {
//                holder.viewPager.currentItem = it
//            }
//        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is PagerItemHolder) {
            viewPageStates.put(holder.absoluteAdapterPosition,holder.viewPager.currentItem)
            super.onViewRecycled(holder)
        }
    }

//    override fun getItemViewType(position: Int): Int = dataList[position].viewType

    override fun getItemCount(): Int = dataList.size

}

/**
 * [RecyclerView.ViewHolder] holding the title of type [TextView]
 */
private class TextItemHolder(view: View) : RecyclerView.ViewHolder(view) {
  val banner_layout :View =  view.findViewById(R.id.banner_layout)
  val tvDesc :View =  view.findViewById(R.id.tvDesc)
  val poll :View =  view.findViewById(R.id.poll)
  val llActions :View =  view.findViewById(R.id.llActions)
  val llChat :View =  view.findViewById(R.id.llChat)
  val tvTag :View =  view.findViewById(R.id.tvTag)
}

/**
 * [RecyclerView.ViewHolder] holding nested [SquareViewPager]
 */
private class PagerItemHolder(view: View) : RecyclerView.ViewHolder(view) {

    val viewPager: SquareViewPager = view.findViewById(R.id.bannerPager)
}
