package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemRecentlyViewedBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecentlyViewedAdapter :
    RecyclerView.Adapter<RecentlyViewedAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemRecentlyViewedBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {

    }

    override fun getItemCount(): Int {
        return /*languageList.size*/8
    }

    inner class CustomViewHolder(var binding: ItemRecentlyViewedBinding) :
        RecyclerView.ViewHolder(binding.root)
}