package agstack.gramophone.ui.advisory.adapter


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemCropIssuesBinding
import agstack.gramophone.ui.advisory.models.cropproblems.GpApiResponseData
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView


class CropIssueListAdapter(public val dataList: List<GpApiResponseData>) :
    RecyclerView.Adapter<CropIssueListAdapter.DeveloperViewHolder>() {
    lateinit var onProblemSelected: (GpApiResponseData) -> Unit?
     var lastSelectedActivityPosition =0
    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemCropIssuesBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        var activityModel: GpApiResponseData = dataList[position]!!
        holder.binding.setVariable(BR.model, activityModel)
        holder.binding.root.setOnClickListener {
            onProblemSelected?.invoke(activityModel)
        }

        if (!activityModel.category_type.equals("Disease")) {
            holder.binding.llDetails.background = AppCompatResources.getDrawable(
                holder.binding.llDetails.context,
                R.drawable.rounded_corner_bottom_16_solid_yellow_stroke_gray
            )
        } else {
            holder.binding.llDetails.background = AppCompatResources.getDrawable(
                holder.binding.llDetails.context,
                R.drawable.rounded_corner_bottom_16_solid_pink_stroke_gray
            )
        }
    }

    override fun getItemCount(): Int {
        return dataList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemCropIssuesBinding) :
        RecyclerView.ViewHolder(binding.root)
}