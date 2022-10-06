package agstack.gramophone.ui.home.adapter

import agstack.gramophone.R
import agstack.gramophone.ui.home.model.Banner
import agstack.gramophone.ui.home.view.fragments.community.LikedPostUserListActivity
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PagerItem
import agstack.gramophone.ui.home.view.fragments.market.model.BannerResponse
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy



class BannerViewPagerAdapter(private  val list: ArrayList<agstack.gramophone.ui.home.view.fragments.market.model.Banner>, private val mContext: Context) : PagerAdapter() {

    private val mInflater: LayoutInflater

    private val mPagerList = ArrayList<Banner>()

    init {
        this.mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val rootView = mInflater.inflate(R.layout.item_banner_pager, container, false)

        val holder = ViewHolder(rootView)

        val data = list[position]

        Glide.with(rootView.context)
            .load(data.bannerImage)
            .into(holder.imageView)
        container.addView(rootView)

        rootView.setOnClickListener {
            //container.context.startActivity(Intent(container.context,LikedPostUserListActivity::class.java))
        }

        return rootView
    }

    override fun getCount(): Int {

        return list.size
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

        var imageView:ImageView

        init {
            imageView= rootView.findViewById(R.id.imageViewViewpager)
        }
    }
}
