package agstack.gramophone.ui.advisory.adapter


import agstack.gramophone.databinding.ItemImageBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class CropIssueImageListAdapter(public val dataList: List<String>) :
    RecyclerView.Adapter<CropIssueImageListAdapter.DeveloperViewHolder>() {

    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemImageBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        var url: String = dataList[position]!!
        Glide.with(holder.binding.root.context).load(url).into(holder.binding.image)

    }

    override fun getItemCount(): Int {
        return dataList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root)
}