package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemReferralBannerBinding
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget


class ReferralBannerAdapter(
    private var bannerList: ArrayList<Banner>,
    private val listener: (String) -> Unit,
) :
    RecyclerView.Adapter<ReferralBannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BannerViewHolder {
        return BannerViewHolder(ItemReferralBannerBinding.inflate(LayoutInflater.from(viewGroup.context)))
    }

    override fun onBindViewHolder(holder: BannerViewHolder, i: Int) {
        /*holder.binding.model = bannerList[i]*/
        /*Glide.with(holder.itemView.context)
            .load(bannerList[i].bannerImage)
            .apply(
                RequestOptions()
                    .error(R.drawable.dummy_referral_banner)
                    .placeholder(R.drawable.dummy_referral_banner)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.ivReferralBanner)*/

        holder.itemView.setOnClickListener {
            listener.invoke("")
        }
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

    inner class BannerViewHolder(var binding: ItemReferralBannerBinding) :
        RecyclerView.ViewHolder(binding.root)
}