package agstack.gramophone.ui.home.adapter

import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import agstack.gramophone.ui.notification.view.URLHandlerActivity
import agstack.gramophone.utils.Constants
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import java.util.*

class ViewPagerAdapter(private val imageList: List<Banner>, private val activity: Activity?) :
    PagerAdapter() {
    // on below line we are creating a method
    // as get count to return the size of the list.
    override fun getCount(): Int {
        return imageList.size
    }

    // on below line we are returning the object
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    // on below line we are initializing
    // our item and inflating our layout file
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // on below line we are initializing
        // our layout inflater.
        val mLayoutInflater =
            container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // on below line we are inflating our custom
        // layout file which we have created.
        val itemView: View = mLayoutInflater.inflate(R.layout.item_header_banner, container, false)

        // on below line we are initializing
        // our image view with the id.
        val imageView: ImageView = itemView.findViewById<View>(R.id.iv_banner) as ImageView


        imageView.setOnClickListener {
            val uri = Uri.parse(imageList[position].bannerLink)
            try {
                val pageName = uri.getQueryParameter("category")

                if (pageName!=null){
                    val intent = Intent(activity, URLHandlerActivity::class.java)
                    intent.putExtra(
                        Constants.URI, imageList[position].bannerLink
                    )
                    activity?.startActivity(intent)
                }

            } catch (ex: Exception) {
                return@setOnClickListener
            }

        sendBannerClickEvent(container.context)
        }
        // on below line we are setting
        // image resource for image view.
        //imageView.setImageResource(imageList[position].id)
        Glide.with(container.context)
            .load(imageList[position].bannerImage)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
        // on the below line we are adding this
        // item view to the container.
        Objects.requireNonNull(container).addView(itemView)

        // on below line we are simply
        // returning our item view.
        return itemView
    }

    private fun sendBannerClickEvent(context: Context) {
        val properties = Properties()
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(context, "KA_Home_Banner_click", properties)

    }

    // on below line we are creating a destroy item method.
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // on below line we are removing view
        container.removeView(`object` as RelativeLayout)
    }
}
