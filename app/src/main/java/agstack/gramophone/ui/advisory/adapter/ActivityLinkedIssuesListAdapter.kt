package agstack.gramophone.ui.advisory.adapter


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemActivityBinding
import agstack.gramophone.databinding.ItemFollowBinding
import agstack.gramophone.databinding.ItemLinkedIssuesBinding
import agstack.gramophone.ui.advisory.models.advisory.Activity
import agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData
import agstack.gramophone.ui.advisory.models.advisory.LinkedIssue
import agstack.gramophone.ui.home.view.fragments.community.model.likes.DataX
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class ActivityLinkedIssuesListAdapter(private val dataList: List<LinkedIssue>) :
    RecyclerView.Adapter<ActivityLinkedIssuesListAdapter.DeveloperViewHolder>() {
    var onActivitySelected: ((LinkedIssue) -> Unit)? = null

    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemLinkedIssuesBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        var linkedIssue: LinkedIssue = dataList[position]!!
        holder.binding.setVariable(BR.model, linkedIssue)

    }

    override fun getItemCount(): Int {
        return dataList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemLinkedIssuesBinding) :
        RecyclerView.ViewHolder(binding.root)
}