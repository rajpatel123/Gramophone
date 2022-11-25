package agstack.gramophone.ui.advisory.adapter


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemActivityBinding
import agstack.gramophone.databinding.ItemFollowBinding
import agstack.gramophone.ui.advisory.models.advisory.Activity
import agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData
import agstack.gramophone.ui.home.view.fragments.community.model.likes.DataX
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class ActivityListAdapter(val dataList: List<GpApiResponseData>) :
    RecyclerView.Adapter<ActivityListAdapter.DeveloperViewHolder>() {
    var onActivitySelected: ((GpApiResponseData) -> Unit)? = null
    var onActivityInfoClicked: ((GpApiResponseData) -> Unit)? = null
     var lastSelectedActivityPosition =0
    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemActivityBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        var activityModel: GpApiResponseData = dataList[position]!!
        holder.binding.setVariable(BR.model, activityModel)

        if (position==0){
            if (!isSelected()){
                lastSelectedActivityPosition = position
                activityModel.isSelected=true
                onActivitySelected?.invoke(activityModel)
            }
        }


        if (activityModel.isSelected){
         holder.binding.flActivity.setBackgroundResource(R.drawable.activity__selected_bg)
         holder.binding.tvActivityQty.setTextColor(
             ContextCompat.getColor(holder.itemView.context,
             R.color.black))
            holder.binding.tvStageName.setTextColor(
             ContextCompat.getColor(holder.itemView.context,
             R.color.black))
        }else{
            holder.binding.flActivity.setBackgroundResource(R.drawable.activity_bg)
            holder.binding.tvActivityQty.setTextColor(
                ContextCompat.getColor(holder.itemView.context,
                    R.color.gray_strike_through))
            holder.binding.tvStageName.setTextColor(
                ContextCompat.getColor(holder.itemView.context,
                    R.color.gray_strike_through))
        }
        holder.binding.flActivity.setOnClickListener {
            dataList[lastSelectedActivityPosition].isSelected=false
            lastSelectedActivityPosition = position
            activityModel.isSelected=true
            onActivitySelected?.invoke(activityModel)
            notifyDataSetChanged()
        }

        holder.binding.ivInfoClicked.setOnClickListener {
            if (activityModel.isSelected){
                onActivityInfoClicked?.invoke(activityModel)
            }
        }
    }

    private fun isSelected(): Boolean {
    var isSelected=false
        run breaking@ {
            dataList.forEach {
                if (it.isSelected){
                    isSelected=true
                    return@breaking
                }
            }
        }
       return isSelected
    }

    override fun getItemCount(): Int {
        return dataList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemActivityBinding) :
        RecyclerView.ViewHolder(binding.root)
}