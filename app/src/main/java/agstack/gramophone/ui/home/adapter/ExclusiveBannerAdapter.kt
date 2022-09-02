package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemExclusiveBannerBinding
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ExclusiveBannerAdapter(private var bannerList: ArrayList<Banner>) :
    RecyclerView.Adapter<ExclusiveBannerAdapter.DeveloperViewHolder>() {
    var itemClicked: ((String) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        return DeveloperViewHolder(
            ItemExclusiveBannerBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, i: Int) {
        holder.binding.model = bannerList[i]
        if (i % 2 == 0) {
            holder.binding.viewSeparator.visibility = View.GONE
        } else {
            holder.binding.viewSeparator.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            itemClicked?.invoke("")
        }
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

    inner class DeveloperViewHolder(var binding: ItemExclusiveBannerBinding) :
        RecyclerView.ViewHolder(binding.root)
}