package agstack.gramophone.ui.home.adapter

import agstack.gramophone.R
import agstack.gramophone.ui.home.view.fragments.community.model.PagerItem
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import java.util.ArrayList



class BannerViewPagerAdapter(list: List<PagerItem>?, private val mContext: Context) : PagerAdapter() {

    private val mInflater: LayoutInflater

    private val mPagerList = ArrayList<PagerItem>()

    init {

        if (list != null)
            mPagerList.addAll(list)

        this.mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val rootView = mInflater.inflate(R.layout.item_banner_pager, container, false)

        val holder = ViewHolder(rootView)

        val data = mPagerList[position]

        holder.pagerText.text ="Viewpager Item Number "+ data.itemText
        Glide.with(rootView.context)
            .load(data.itemImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .fitCenter()
            .into(holder.imageView)
        container.addView(rootView)

        return rootView
    }

    override fun getCount(): Int {

        return mPagerList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // Don't call the super
        // super.destroyItem(container, position, object);

        container.removeView(`object` as View)
    }

    /**
     * [android.support.v7.widget.RecyclerView.ViewHolder]
     */
    internal inner class ViewHolder(rootView: View) {

        var pagerText: TextView
        var imageView:ImageView

        init {
            pagerText = rootView.findViewById(R.id.tvPager)
            imageView= rootView.findViewById(R.id.imageViewViewpager)
        }
    }
}
