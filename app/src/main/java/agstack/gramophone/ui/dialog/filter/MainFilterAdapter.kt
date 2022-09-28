package agstack.gramophone.ui.dialog.filter


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemMainFilterBinding
import agstack.gramophone.databinding.ItemOtherStateBinding
import agstack.gramophone.ui.address.model.State
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView

class MainFilterAdapter(
    list: ArrayList<MainFilterData>,
    private val listener: (String, Int) -> Unit,
) :
    RecyclerView.Adapter<MainFilterAdapter.CustomViewHolder>() {
    private val mainFilterList = list
    var lastSelectPosition: Int = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemMainFilterBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val mainFilterData: MainFilterData = mainFilterList[position]
        holder.binding.setVariable(BR.model, mainFilterData)
        val mBinding = holder.binding as ItemMainFilterBinding
        if (mainFilterList[position].isSelected) {
            holder.binding.tvFilterName.setTypeface(ResourcesCompat.getFont(holder.itemView.context, R.font.manrope_bold))
        } else {
            holder.binding.tvFilterName.setTypeface(ResourcesCompat.getFont(holder.itemView.context, R.font.manrope_regular))
        }
        mBinding.item.setOnClickListener {
            mainFilterList[lastSelectPosition].isSelected = false
            lastSelectPosition = position
            mainFilterData.isSelected = true
            listener.invoke(mainFilterData.name, position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mainFilterList.size
    }

    inner class CustomViewHolder(var binding: ItemMainFilterBinding) :
        RecyclerView.ViewHolder(binding.root)
}